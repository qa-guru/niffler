package niffler.test.rest;

import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Step;
import niffler.api.NifflerAuthClient;
import niffler.api.context.SessionStorageHolder;
import niffler.jupiter.annotation.meta.RestTest;

@RestTest
public abstract class BaseRestTest {

    protected static final String ID_REGEXP = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    protected final NifflerAuthClient authClient = new NifflerAuthClient();

    @Step("Login to niffler using api")
    protected void apiLogin(String username, String password) throws Exception {
        authClient.authorize();
        authClient.login(username, password);
        JsonNode token = authClient.getToken();
        SessionStorageHolder.getInstance().addToken(token.get("id_token").asText());
    }
}
