package niffler.test.soap;

import io.qameta.allure.AllureId;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.User;
import niffler.model.UserJson;
import niffler.ws.NifflerUserdataWsService;
import niffler.ws.model.wsdl.Currency;
import niffler.ws.model.wsdl.CurrentUserRequest;
import niffler.ws.model.wsdl.CurrentUserResponse;
import niffler.ws.model.wsdl.FriendState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static niffler.jupiter.extension.CreateUserExtension.Selector.METHOD;

public class UserDataSoapTest extends BaseSoapTest {

    private static final String ID_REGEXP = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    private final NifflerUserdataWsService nus = new NifflerUserdataWsService();

    @Test
    @AllureId("100001")
    @GenerateUser()
    void currentUserTest(@User(selector = METHOD) UserJson user) throws Exception {
        CurrentUserRequest cur = new CurrentUserRequest();
        cur.setUsername(user.getUsername());

        CurrentUserResponse currentUserResponse = nus.currentUser(cur);

        Assertions.assertTrue(currentUserResponse.getUser().getId().matches(ID_REGEXP));
        Assertions.assertEquals(user.getUsername(), currentUserResponse.getUser().getUsername());
        Assertions.assertEquals(Currency.RUB, currentUserResponse.getUser().getCurrency());
        Assertions.assertEquals(FriendState.VOID, currentUserResponse.getUser().getFriendState());
    }
}
