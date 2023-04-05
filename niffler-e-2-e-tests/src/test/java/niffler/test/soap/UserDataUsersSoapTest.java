package niffler.test.soap;

import io.qameta.allure.AllureId;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.User;
import niffler.model.rest.UserJson;
import niffler.model.soap.AllUsersRequest;
import niffler.model.soap.AllUsersResponse;
import niffler.model.soap.Currency;
import niffler.model.soap.CurrentUserRequest;
import niffler.model.soap.CurrentUserResponse;
import niffler.model.soap.FriendState;
import niffler.model.soap.UpdateUserInfoRequest;
import niffler.model.soap.UpdateUserInfoResponse;
import niffler.ws.NifflerUserdataWsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static niffler.jupiter.extension.CreateUserExtension.Selector.METHOD;

public class UserDataUsersSoapTest extends BaseSoapTest {

    private static final String ID_REGEXP = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    private final NifflerUserdataWsService nus = new NifflerUserdataWsService();

    @Test
    @DisplayName("SOAP: Для нового пользователя долна возвращаться информация из niffler-userdata c дефолтными значениями")
    @AllureId("100001")
    @Tag("SOAP")
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

    @Test
    @DisplayName("SOAP: При обновлении юзера должны сохраняться значения в niffler-userdata")
    @AllureId("100002")
    @Tag("SOAP")
    @GenerateUser()
    void updateUserTest(@User(selector = METHOD) UserJson user) throws Exception {
        final String firstName = "FirstName";
        final String secondName = "SecondName";

        UpdateUserInfoRequest uir = new UpdateUserInfoRequest();
        niffler.model.soap.User xmlUser = new niffler.model.soap.User();
        xmlUser.setUsername(user.getUsername());
        xmlUser.setCurrency(Currency.EUR);
        xmlUser.setFirstname(firstName);
        xmlUser.setSurname(secondName);
        uir.setUser(xmlUser);

        UpdateUserInfoResponse updateUserInfoResponse = nus.updateUserInfo(uir);

        Assertions.assertTrue(updateUserInfoResponse.getUser().getId().matches(ID_REGEXP));
        Assertions.assertEquals(user.getUsername(), updateUserInfoResponse.getUser().getUsername());
        Assertions.assertEquals(Currency.EUR, updateUserInfoResponse.getUser().getCurrency());
        Assertions.assertEquals(firstName, updateUserInfoResponse.getUser().getFirstname());
        Assertions.assertEquals(secondName, updateUserInfoResponse.getUser().getSurname());
    }

    @Test
    @DisplayName("SOAP: Список всех пользователей системы не должен быть пустым")
    @AllureId("100003")
    @Tag("SOAP")
    @GenerateUser()
    void allUsersTest(@User(selector = METHOD) UserJson user) throws Exception {
        AllUsersRequest aur = new AllUsersRequest();
        aur.setUsername(user.getUsername());

        AllUsersResponse allUsersResponse = nus.allUsersRequest(aur);

        Assertions.assertFalse(allUsersResponse.getUser().isEmpty());
    }
}
