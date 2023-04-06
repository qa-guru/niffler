package niffler.test.soap;

import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static niffler.jupiter.extension.CreateUserExtension.Selector.METHOD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("[SOAP][niffler-userdata]: Пользователи")
@DisplayName("[SOAP][niffler-userdata]: Пользователи")
public class UserDataUsersSoapTest extends BaseSoapTest {

    private static final String ID_REGEXP = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    private final NifflerUserdataWsService nus = new NifflerUserdataWsService();

    @Test
    @DisplayName("SOAP: Для нового пользователя должна возвращаться информация из niffler-userdata c дефолтными значениями")
    @AllureId("100001")
    @Tag("SOAP")
    @GenerateUser()
    void currentUserTest(@User(selector = METHOD) UserJson user) throws Exception {
        CurrentUserRequest cur = new CurrentUserRequest();
        cur.setUsername(user.getUsername());

        final CurrentUserResponse currentUserResponse = nus.currentUser(cur);

        step("Check that response contains ID (GUID)", () ->
                assertTrue(currentUserResponse.getUser().getId().matches(ID_REGEXP))
        );
        step("Check that response contains username", () ->
                assertEquals(user.getUsername(), currentUserResponse.getUser().getUsername())
        );
        step("Check that response contains default currency (RUB)", () ->
                assertEquals(Currency.RUB, currentUserResponse.getUser().getCurrency())
        );
        step("Check that response contains default friends state (VOID), only for SOAP", () ->
                assertEquals(FriendState.VOID, currentUserResponse.getUser().getFriendState())
        );
    }

    @Test
    @DisplayName("SOAP: При обновлении юзера должны сохраняться значения в niffler-userdata")
    @AllureId("100002")
    @Tag("SOAP")
    @GenerateUser()
    void updateUserTest(@User(selector = METHOD) UserJson user) throws Exception {
        final String firstName = "Pizzly";
        final String secondName = "Pizzlyvich";

        UpdateUserInfoRequest uir = new UpdateUserInfoRequest();
        niffler.model.soap.User xmlUser = new niffler.model.soap.User();
        xmlUser.setUsername(user.getUsername());
        xmlUser.setCurrency(Currency.USD);
        xmlUser.setFirstname(firstName);
        xmlUser.setSurname(secondName);
        uir.setUser(xmlUser);

        final UpdateUserInfoResponse updateUserInfoResponse = nus.updateUserInfo(uir);

        step("Check that response contains ID (GUID)", () ->
                assertTrue(updateUserInfoResponse.getUser().getId().matches(ID_REGEXP))
        );
        step("Check that response contains username", () ->
                assertEquals(user.getUsername(), updateUserInfoResponse.getUser().getUsername())
        );
        step("Check that response contains updated currency (USD)", () ->
                assertEquals(Currency.USD, updateUserInfoResponse.getUser().getCurrency())
        );
        step("Check that response contains updated firstname (Pizzly)", () ->
                assertEquals(firstName, updateUserInfoResponse.getUser().getFirstname())
        );
        step("Check that response contains updated surname (Pizzlyvich)", () ->
                assertEquals(secondName, updateUserInfoResponse.getUser().getSurname())
        );
    }

    @Test
    @DisplayName("SOAP: Список всех пользователей системы не должен быть пустым")
    @AllureId("100003")
    @Tag("SOAP")
    @GenerateUser()
    void allUsersTest(@User(selector = METHOD) UserJson user) throws Exception {
        AllUsersRequest aur = new AllUsersRequest();
        aur.setUsername(user.getUsername());

        final AllUsersResponse allUsersResponse = nus.allUsersRequest(aur);

        step("Check that all users list is not empty", () ->
                assertFalse(allUsersResponse.getUser().isEmpty())
        );
    }
}
