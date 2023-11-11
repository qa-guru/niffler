package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.github.javafaker.Faker;
import guru.qa.niffler.api.client.AuthClient;
import guru.qa.niffler.api.client.DBUser7RestClient;
import guru.qa.niffler.api.client.RegisterClient;
import guru.qa.niffler.api.context.CookieContext;
import guru.qa.niffler.api.context.SessionContext;
import guru.qa.niffler.api.util.OauthUtils;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiRegisterWithRandomUser;
import guru.qa.niffler.jupiter.annotation.GenerateRandomUser;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import java.util.Objects;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.openqa.selenium.Cookie;

public class GenerateRandomUserExtension implements BeforeEachCallback, AfterTestExecutionCallback,
    ParameterResolver {

  private final static Config CFG = Config.getInstance();
  private final RegisterClient registerClient = new RegisterClient();
  private final AuthClient authClient = new AuthClient();
  private final String JSESSIONID = "JSESSIONID";
  public final static Namespace NAMESPACE = Namespace.create(DBUserExtension.class);
  private final DBUser7RestClient dbUser7RestClient = new DBUser7RestClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    final String testId = getTestId(context);
    GenerateRandomUser apiRegister = context.getRequiredTestMethod().getAnnotation(GenerateRandomUser.class);
    if (apiRegister != null) {

      String randomUsername = new Faker().name().username();
      String randomPassword = new Faker().code().gtin8();

      UserJson registeredUser = doRegister(
          randomUsername,
          randomPassword,
          randomPassword
      );
      doLogin(
          randomUsername,
          randomPassword
      );

      context.getStore(NAMESPACE).put(testId, registeredUser);
    }
  }

  private UserJson doRegister(String username, String password, String submitPassword) {
    registerClient.registerPreRequest();
    registerClient.register(username, password, submitPassword);
    return dbUser7RestClient.getCurrentUser(username);
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

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
  }

  @Override
  public UserJson resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    final String testId = getTestId(extensionContext);
    return extensionContext.getStore(NAMESPACE).get(testId, UserJson.class);
  }

  private String getTestId(ExtensionContext context) {
    return Objects.requireNonNull(
        context.getRequiredTestMethod().getAnnotation(AllureId.class)
    ).value();
  }
}
