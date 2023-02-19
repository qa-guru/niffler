package niffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.fasterxml.jackson.databind.JsonNode;
import niffler.api.context.CookieHolder;
import niffler.api.NifflerLoginClient;
import niffler.api.context.SessionStorage;
import niffler.config.Config;
import niffler.jupiter.annotation.ApiLogin;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;

public class ApiLoginExtention implements BeforeEachCallback, AfterEachCallback {

    private final NifflerLoginClient loginClient = new NifflerLoginClient();
    protected static final Config CFG = Config.getConfig();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        ApiLogin annotation = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);
        String username = annotation.username();
        String password = annotation.password();
        apiLogin(username, password);
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
        loginClient.authorize();
        loginClient.login(username, password);
        JsonNode token = loginClient.getToken();
        SessionStorage.getInstance().addToken(token.get("id_token").asText());
    }
}
