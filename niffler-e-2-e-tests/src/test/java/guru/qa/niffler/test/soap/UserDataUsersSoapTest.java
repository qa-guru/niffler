package guru.qa.niffler.test.soap;

import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.userdata.wsdl.AllUsersRequest;
import guru.qa.niffler.userdata.wsdl.Currency;
import guru.qa.niffler.userdata.wsdl.CurrentUserRequest;
import guru.qa.niffler.userdata.wsdl.FriendState;
import guru.qa.niffler.userdata.wsdl.UpdateUserRequest;
import guru.qa.niffler.userdata.wsdl.UserResponse;
import guru.qa.niffler.userdata.wsdl.UsersResponse;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static guru.qa.niffler.jupiter.annotation.User.Selector.METHOD;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("[SOAP][niffler-userdata]: Пользователи")
@DisplayName("[SOAP][niffler-userdata]: Пользователи")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDataUsersSoapTest extends BaseSoapTest {

    @Test
    @DisplayName("SOAP: Для нового пользователя должна возвращаться информация из niffler-userdata c дефолтными значениями")
    @AllureId("100001")
    @Tag("SOAP")
    @GenerateUser()
    void currentUserTest(@User(selector = METHOD) UserJson user) throws Exception {
        CurrentUserRequest cur = new CurrentUserRequest();
        cur.setUsername(user.username());

        final UserResponse currentUserResponse = wsClient.currentUser(cur);

        step("Check that response contains ID (GUID)", () ->
                assertTrue(currentUserResponse.getUser().getId().matches(ID_REGEXP))
        );
        step("Check that response contains username", () ->
                assertEquals(user.username(), currentUserResponse.getUser().getUsername())
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

        UpdateUserRequest uir = new UpdateUserRequest();
        guru.qa.niffler.userdata.wsdl.User xmlUser = new guru.qa.niffler.userdata.wsdl.User();
        xmlUser.setUsername(user.username());
        xmlUser.setCurrency(Currency.USD);
        xmlUser.setFirstname(firstName);
        xmlUser.setSurname(secondName);
        uir.setUser(xmlUser);

        final UserResponse updateUserInfoResponse = wsClient.updateUserInfo(uir);

        step("Check that response contains ID (GUID)", () ->
                assertTrue(updateUserInfoResponse.getUser().getId().matches(ID_REGEXP))
        );
        step("Check that response contains username", () ->
                assertEquals(user.username(), updateUserInfoResponse.getUser().getUsername())
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
    @Order(1)
    void allUsersTest(@User(selector = METHOD) UserJson user) throws Exception {
        AllUsersRequest aur = new AllUsersRequest();
        aur.setUsername(user.username());

        final UsersResponse allUsersResponse = wsClient.allUsersRequest(aur);

        step("Check that all users list is not empty", () ->
                assertFalse(allUsersResponse.getUser().isEmpty())
        );
    }
}
