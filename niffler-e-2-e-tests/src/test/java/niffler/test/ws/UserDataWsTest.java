package niffler.test.ws;

import io.qameta.allure.AllureId;
import niffler.ws.NifflerUserdataWsService;
import niffler.ws.model.wsdl.CurrentUserRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class UserDataWsTest {

    private final NifflerUserdataWsService nus = new NifflerUserdataWsService();

    @Test
    // todo implement!
    @Disabled
    @AllureId("101")
    void updateUserApiTest() throws Exception {
        RequestEnvelope envelope = new RequestEnvelope();
        CurrentUserRequest request = new CurrentUserRequest();
        request.setUsername("pizzly");
        RequestBody body = new RequestBody();
        body.setRequestData(request);
        envelope.setBody(body);
    }
}
