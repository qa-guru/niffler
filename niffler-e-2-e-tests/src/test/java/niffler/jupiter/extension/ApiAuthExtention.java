package niffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.AllureId;
import niffler.api.context.CookieHolder;
import niffler.api.NifflerAuthClient;
import niffler.api.context.SessionStorage;
import niffler.config.Config;
import niffler.jupiter.annotation.ApiLogin;
import niffler.jupiter.annotation.GenerateUser;
import niffler.model.UserJson;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.openqa.selenium.Cookie;
import retrofit2.Response;

import java.util.Objects;

import static niffler.api.utils.DataUtils.generateRandomPassword;
import static niffler.api.utils.DataUtils.generateRandomUsername;

public class ApiAuthExtention implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    private final NifflerAuthClient authClient = new NifflerAuthClient();
    protected static final Config CFG = Config.getConfig();

    public static final ExtensionContext.Namespace USERS_NAMESPACE
            = ExtensionContext.Namespace.create(ApiAuthExtention.class);

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        String testId = getTestId(extensionContext);
        return (UserJson) extensionContext.getStore(USERS_NAMESPACE).get(testId);
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        ApiLogin annotation = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);
        GenerateUser generateUserAnnotation = annotation.nifflerUser();
        if ((!generateUserAnnotation.handleAnnotation() && "".equals(annotation.username()) && "".equals(annotation.password()))) {
            throw new IllegalArgumentException("You have to provide in @ApiLogin annotation user by username/password or @NifflerUser");
        }
        String testId = getTestId(context);
        String username;
        String password;
        if(generateUserAnnotation.handleAnnotation()) {
            username = generateUserAnnotation.username();
            password = generateUserAnnotation.password();
            if("".equals(username)) {
                username = generateRandomUsername();
            }
            if("".equals(password)) {
                password = generateRandomPassword();
            }
            apiRegister(username, password);
        } else {
            username = annotation.username();
            password = annotation.password();
        }
        apiLogin(username, password);
        UserJson userJson = new UserJson();
        userJson.setUserName(username);
        userJson.setPassword(password);
        context.getStore(USERS_NAMESPACE).put(testId, userJson);
        Selenide.open(CFG.frontUrl());
        com.codeborne.selenide.SessionStorage sessionStorage = Selenide.sessionStorage();
        sessionStorage.setItem("codeChallenge", SessionStorage.getInstance().getCodeChallenge());
        sessionStorage.setItem("id_token", SessionStorage.getInstance().getToken());
        sessionStorage.setItem("codeVerifier", SessionStorage.getInstance().getCodeVerifier());

        WebDriverRunner.getWebDriver().manage()
                .addCookie(new Cookie("JSESSIONID", CookieHolder.getInstance().getCookieValueByPart("JSESSIONID")));
    }

    @Override
    public void afterEach(ExtensionContext context) {
        Selenide.closeWebDriver();
    }

    private void apiLogin(String username, String password) throws Exception{
        authClient.authorize();
        authClient.login(username, password);
        JsonNode token = authClient.getToken();
        SessionStorage.getInstance().addToken(token.get("id_token").asText());
    }

    private void apiRegister(String username, String password) throws Exception{
        authClient.authorize();
        Response<Void> res = authClient.register(username, password);
        if (res.code() != 201) {
            throw new RuntimeException("User is not registered");
        }
    }

    private String getTestId(ExtensionContext context) {
        return Objects.requireNonNull(
                context.getRequiredTestMethod().getAnnotation(AllureId.class)
        ).value();
    }
}
