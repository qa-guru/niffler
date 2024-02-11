package guru.qa.niffler.jupiter.extension;

import static guru.qa.niffler.jupiter.extension.CreateUserExtension.API_LOGIN_USERS_NAMESPACE;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.client.NifflerAuthClient;
import guru.qa.niffler.jupiter.annotation.ApiLoginNew;
import guru.qa.niffler.jupiter.annotation.GenerateUserNew;
import io.qameta.allure.AllureId;
import guru.qa.niffler.api.context.CookieHolder;
import guru.qa.niffler.api.context.SessionStorageHolder;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;

import java.util.Objects;

public class ApiAuth7Extension implements BeforeEachCallback, AfterTestExecutionCallback {

  private final NifflerAuthClient authClient = new NifflerAuthClient();
  protected static final Config CFG = Config.getInstance();

  public static final ExtensionContext.Namespace AUTH_EXTENSION_NAMESPACE
      = ExtensionContext.Namespace.create(ApiAuth7Extension.class);

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    ApiLoginNew apiLoginAnnotation = context.getRequiredTestMethod().getAnnotation(ApiLoginNew.class);
    GenerateUserNew generateUserAnnotation = apiLoginAnnotation.nifflerUser();
    if ((!generateUserAnnotation.handleAnnotation() && "".equals(apiLoginAnnotation.username()) && "".equals(apiLoginAnnotation.password()))) {
      throw new IllegalArgumentException("You have to provide in @ApiLogin annotation user by username/password or @GenerateUser");
    }
    String testId = getTestId(context);

    UserJson userToLogin;
    if (generateUserAnnotation.handleAnnotation()) {
      userToLogin = context.getStore(API_LOGIN_USERS_NAMESPACE).get(testId, UserJson.class);
    } else {
      userToLogin = new UserJson();
      userToLogin.setUserName(apiLoginAnnotation.username());
      userToLogin.setPassword(apiLoginAnnotation.password());
    }
    apiLogin(userToLogin.getUserName(), userToLogin.getPassword());
    Selenide.open(CFG.getFrontUrl());
    com.codeborne.selenide.SessionStorage sessionStorage = Selenide.sessionStorage();
    sessionStorage.setItem("codeChallenge", SessionStorageHolder.getInstance().getCodeChallenge());
    sessionStorage.setItem("id_token", SessionStorageHolder.getInstance().getToken());
    sessionStorage.setItem("codeVerifier", SessionStorageHolder.getInstance().getCodeVerifier());

    WebDriverRunner.getWebDriver().manage()
        .addCookie(new Cookie("JSESSIONID", CookieHolder.getInstance().getCookieValueByPart("JSESSIONID")));
  }

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {
    CookieHolder.getInstance().flushAll();
    SessionStorageHolder.getInstance().flushAll();
  }

  private void apiLogin(String username, String password) throws Exception {
    authClient.authorize();
    authClient.login(username, password);
    JsonNode token = authClient.getToken();
    SessionStorageHolder.getInstance().addToken(token.get("id_token").asText());
  }

  private String getTestId(ExtensionContext context) {
    return Objects.requireNonNull(
        context.getRequiredTestMethod().getAnnotation(AllureId.class)
    ).value();
  }
}