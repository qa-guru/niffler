package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.api.AuthApiClient;
import guru.qa.niffler.api.service.ThreadLocalCookieStore;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.model.rest.TestData;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.utils.OauthUtils;
import io.qameta.allure.Step;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;
import org.openqa.selenium.Cookie;

import javax.annotation.Nonnull;

import static guru.qa.niffler.jupiter.annotation.User.Selector.NESTED;

public class ApiLoginExtension implements BeforeEachCallback, ParameterResolver {

    protected static final Config CFG = Config.getConfig();

    private static final AuthApiClient authClient = new AuthApiClient();
    private final boolean setUpBrowser;

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(ApiLoginExtension.class);

    public static ApiLoginExtension rest() {
        return new ApiLoginExtension(false);
    }

    public ApiLoginExtension() {
        this.setUpBrowser = true;
    }

    public ApiLoginExtension(boolean setUpBrowser) {
        this.setUpBrowser = setUpBrowser;
    }

    @Step("Login to niffler using api")
    @SuppressWarnings("unchecked")
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        ApiLogin apiLoginAnnotation = AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                ApiLogin.class
        ).orElse(AnnotationSupport.findAnnotation(
                context.getRequiredTestClass(),
                ApiLogin.class
        ).orElse(
                null
        ));
        if (apiLoginAnnotation != null) {
            GenerateUser generateUserAnnotation = apiLoginAnnotation.user();
            if ((!generateUserAnnotation.handleAnnotation()
                    && "".equals(apiLoginAnnotation.username())
                    && "".equals(apiLoginAnnotation.password())
            )) {
                throw new IllegalArgumentException("You have to provide in @ApiLogin annotation user by username/password or @GenerateUser");
            }

            UserJson userToLogin;
            if (generateUserAnnotation.handleAnnotation()) {
                userToLogin = AbstractCreateUserExtension.createdUsers(context, NESTED).getFirst();
            } else {
                userToLogin = new UserJson(apiLoginAnnotation.username(), new TestData(
                        apiLoginAnnotation.password(),
                        null,
                        null,
                        null,
                        null,
                        null)
                );
            }
            setCodeVerifier(context, OauthUtils.codeVerifier());
            setCodeChallenge(context, OauthUtils.codeChallenge(getCodeVerifier(context)));
            authClient.login(context, userToLogin.username(), userToLogin.testData().password());

            if (setUpBrowser) {
                Selenide.open(CFG.frontUrl());
                Selenide.sessionStorage().setItem("id_token", getToken(context));
                WebDriverRunner.getWebDriver().manage().addCookie(getJsessionIdCookie());
                Selenide.refresh();
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return AnnotationSupport.isAnnotated(parameterContext.getParameter(), Token.class) &&
                parameterContext.getParameter().getType().isAssignableFrom(String.class);
    }

    @Override
    public String resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return AnnotationSupport.findAnnotation(parameterContext.getParameter(), Token.class).orElseThrow().bearer()
                ? getBearerToken(extensionContext)
                : getToken(extensionContext);
    }

    public static void setCodeVerifier(@Nonnull ExtensionContext extensionContext, @Nonnull String codeVerifier) {
        extensionContext.getStore(NAMESPACE).put("cv", codeVerifier);
    }

    public static String getCodeVerifier(@Nonnull ExtensionContext extensionContext) {
        return extensionContext.getStore(NAMESPACE).get("cv", String.class);
    }

    public static void setCodeChallenge(@Nonnull ExtensionContext context, @Nonnull String codeChallenge) {
        context.getStore(NAMESPACE).put("cc", codeChallenge);
    }

    public static String getCodeChallenge(@Nonnull ExtensionContext context) {
        return context.getStore(NAMESPACE).get("cc", String.class);
    }

    public static void setCode(@Nonnull ExtensionContext context, @Nonnull String code) {
        context.getStore(NAMESPACE).put("code", code);
    }

    public static String getCode(@Nonnull ExtensionContext context) {
        return context.getStore(NAMESPACE).get("code", String.class);
    }

    public static void setToken(@Nonnull ExtensionContext context, @Nonnull String token) {
        context.getStore(NAMESPACE).put("token", token);
    }

    public static String getToken(@Nonnull ExtensionContext context) {
        return context.getStore(NAMESPACE).get("token", String.class);
    }

    public static String getBearerToken(@Nonnull ExtensionContext context) {
        return "Bearer " + getToken(context);
    }

    public static String getCsrf() {
        return ThreadLocalCookieStore.INSTANCE.cookieValue("XSRF-TOKEN");
    }

    public static String getJsessionid() {
        return ThreadLocalCookieStore.INSTANCE.cookieValue("JSESSIONID");
    }

    public Cookie getJsessionIdCookie() {
        return new Cookie(
                "JSESSIONID",
                getJsessionid()
        );
    }

    public static String getJsessionIdCookieAsString() {
        return "JSESSIONID=" + getJsessionid();
    }
}
