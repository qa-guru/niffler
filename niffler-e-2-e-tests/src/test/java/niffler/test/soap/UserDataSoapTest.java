package niffler.test.soap;

import io.qameta.allure.AllureId;
import niffler.ws.NifflerUserdataWsService;
import niffler.ws.model.wsdl.CurrentUserRequest;
import niffler.ws.model.wsdl.CurrentUserResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserDataSoapTest extends BaseSoapTest {

    private final NifflerUserdataWsService nus = new NifflerUserdataWsService();

    @Test
    @AllureId("100001")
    void currentUserTest() throws Exception {
        CurrentUserRequest cur = new CurrentUserRequest();
        cur.setUsername("dima");
        CurrentUserResponse currentUserResponse = nus.currentUser(cur);
        Assertions.assertEquals("Dmitrii", currentUserResponse.getUser().getFirstname());
    }
}
