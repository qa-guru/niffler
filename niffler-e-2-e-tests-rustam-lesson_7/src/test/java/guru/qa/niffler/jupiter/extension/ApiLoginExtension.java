package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.api.client.AuthClient;
import guru.qa.niffler.api.context.CookieContext;
import guru.qa.niffler.api.context.SessionContext;
import guru.qa.niffler.api.util.OauthUtils;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;

public class ApiLoginExtension implements BeforeEachCallback, AfterTestExecutionCallback {

  protected static final Config CFG = Config.getInstance();
  private final AuthClient authClient = new AuthClient();
  private final static String JSESSIONID = "JSESSIONID";

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    ApiLogin apiLogin = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);
    if (apiLogin != null) {
      doLogin(apiLogin.username(), apiLogin.password());
    }
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
    Selenide.sessionStorage().setItem("codeChallenge", sessionContext.getCodeChallenge());
    Selenide.sessionStorage().setItem("codeVerifier", sessionContext.getCodeVerifier());
    Cookie jsessionCookie = new Cookie(JSESSIONID, cookieContext.getCookie(JSESSIONID));
    WebDriverRunner.getWebDriver().manage().addCookie(jsessionCookie);
  }

  @Override
  public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
    SessionContext.getInstance().release();
    CookieContext.getInstance().release();
  }
}
