package niffler.test.rest;

import com.fasterxml.jackson.databind.JsonNode;
import niffler.api.NifflerAuthClient;
import niffler.api.context.SessionStorageHolder;

public abstract class RestTest {

    protected final NifflerAuthClient authClient = new NifflerAuthClient();

    protected void apiLogin(String username, String password) throws Exception {
        authClient.authorize();
        authClient.login(username, password);
        JsonNode token = authClient.getToken();
        SessionStorageHolder.getInstance().addToken(token.get("id_token").asText());
    }
}
