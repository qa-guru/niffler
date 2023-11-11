package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.api.client.AuthClient;
import guru.qa.niffler.api.client.RegisterClient;
import guru.qa.niffler.api.context.CookieContext;
import guru.qa.niffler.api.context.SessionContext;
import guru.qa.niffler.api.util.OauthUtils;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiRegister;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;

public class ApiRegisterExtension implements BeforeEachCallback, AfterTestExecutionCallback {

  private final static Config CFG = Config.getInstance();
  private final RegisterClient registerClient = new RegisterClient();
  private final AuthClient authClient = new AuthClient();
  private final String JSESSIONID = "JSESSIONID";

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    ApiRegister apiRegister = context.getRequiredTestMethod().getAnnotation(ApiRegister.class);
    if (apiRegister != null) {
      doRegister(
          apiRegister.username(),
          apiRegister.password(),
          apiRegister.submitPassword()
      );
      doLogin(
          apiRegister.username(),
          apiRegister.password()
      );
    }
  }

  private void doRegister(String username, String password, String submitPassword) {
    registerClient.registerPreRequest();
    registerClient.register(username, password, submitPassword);
  }

  private void doLogin(String username, String password) {
    final SessionContext sessionContext = SessionContext.getInstance();
    final CookieContext cookieContext = CookieContext.getInstance();
    final String codeVerifier = OauthUtils.generateCodeVerifier();
    final String codeChallenge = OauthUtils.generateCodeChallenge(codeVerifier);

    sessionContext.setCodeVerifier(codeVerifier);
    sessionContext.setCodeChallenge(codeChallenge);

    authClient.authorizePreRequest();
    authClient.login(username, password);
    final String token = authClient.getToken();
    Selenide.open(CFG.getFrontUrl());
    Selenide.sessionStorage().setItem("id_token", token);
    Selenide.sessionStorage().setItem("codeVerifier", sessionContext.getCodeVerifier());
    Selenide.sessionStorage().setItem("codeChallenge", sessionContext.getCodeChallenge());
    Cookie jsessionCookie = new Cookie(JSESSIONID, cookieContext.getCookie(JSESSIONID));
    WebDriverRunner.getWebDriver().manage().addCookie(jsessionCookie);
  }

  @Override
  public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
    SessionContext.getInstance().release();
    CookieContext.getInstance().release();
  }
}
