package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.NifflerAuthClient;
import guru.qa.niffler.api.context.CookieHolder;
import guru.qa.niffler.api.context.SessionStorageHolder;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.Step;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;

import java.util.Objects;

import static guru.qa.niffler.jupiter.extension.CreateUserExtension.API_LOGIN_USERS_NAMESPACE;

public class ApiAuthExtension implements BeforeEachCallback {

    private final NifflerAuthClient authClient = new NifflerAuthClient();
    protected static final Config CFG = Config.getConfig();

    public static final ExtensionContext.Namespace AUTH_EXTENSION_NAMESPACE
            = ExtensionContext.Namespace.create(ApiAuthExtension.class);

    @Step("Login to niffler using api")
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        ApiLogin apiLoginAnnotation = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);
        GenerateUser generateUserAnnotation = apiLoginAnnotation.nifflerUser();
        if ((!generateUserAnnotation.handleAnnotation() && "".equals(apiLoginAnnotation.username()) && "".equals(apiLoginAnnotation.password()))) {
            throw new IllegalArgumentException("You have to provide in @ApiLogin annotation user by username/password or @GenerateUser");
        }
        String testId = getTestId(context);

        UserJson userToLogin;
        if (generateUserAnnotation.handleAnnotation()) {
            userToLogin = context.getStore(API_LOGIN_USERS_NAMESPACE).get(testId, UserJson.class);
        } else {
            userToLogin = new UserJson();
            userToLogin.setUsername(apiLoginAnnotation.username());
            userToLogin.setPassword(apiLoginAnnotation.password());
        }
        try {
            apiLogin(userToLogin.getUsername(), userToLogin.getPassword());
            Selenide.open(CFG.frontUrl());
            com.codeborne.selenide.SessionStorage sessionStorage = Selenide.sessionStorage();
            sessionStorage.setItem("codeChallenge", SessionStorageHolder.getInstance().getCodeChallenge());
            sessionStorage.setItem("id_token", SessionStorageHolder.getInstance().getToken());
            sessionStorage.setItem("codeVerifier", SessionStorageHolder.getInstance().getCodeVerifier());

            WebDriverRunner.getWebDriver().manage()
                    .addCookie(new Cookie("JSESSIONID", CookieHolder.getInstance().getCookieValueByPart("JSESSIONID")));
        } catch (Exception e) {
            CookieHolder.getInstance().flushAll();
            SessionStorageHolder.getInstance().flushAll();
            throw e;
        }
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
