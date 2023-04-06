package niffler.test.rest;

import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import niffler.api.NifflerUserdataClient;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.User;
import niffler.model.rest.CurrencyValues;
import niffler.model.rest.UserJson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static niffler.jupiter.extension.CreateUserExtension.Selector.METHOD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("[REST][niffler-userdata]: Пользователи")
@DisplayName("[REST][niffler-userdata]: Пользователи")
public class UserDataUsersRestTest extends BaseRestTest {

    private static final String ID_REGEXP = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    private final NifflerUserdataClient nus = new NifflerUserdataClient();

    @Test
    @DisplayName("REST: Для нового пользователя должна возвращаться информация из niffler-userdata c дефолтными значениями")
    @AllureId("200001")
    @Tag("REST")
    @GenerateUser()
    void currentUserTest(@User(selector = METHOD) UserJson user) throws Exception {
        final UserJson currentUserResponse = nus.getCurrentUser(user.getUsername());

        step("Check that response contains ID (GUID)", () ->
                assertTrue(currentUserResponse.getId().toString().matches(ID_REGEXP))
        );
        step("Check that response contains username", () ->
                assertEquals(user.getUsername(), currentUserResponse.getUsername())
        );
        step("Check that response contains default currency (RUB)", () ->
                assertEquals(CurrencyValues.RUB, currentUserResponse.getCurrency())
        );
    }

    @Test
    @DisplayName("REST: При обновлении юзера должны сохраняться значения в niffler-userdata")
    @AllureId("200002")
    @Tag("REST")
    @GenerateUser()
    void updateUserTest(@User(selector = METHOD) UserJson user) throws Exception {
        final String firstName = "Pizzly";
        final String secondName = "Pizzlyvich";

        UserJson jsonUser = new UserJson();
        jsonUser.setUsername(user.getUsername());
        jsonUser.setCurrency(CurrencyValues.KZT);
        jsonUser.setFirstname(firstName);
        jsonUser.setSurname(secondName);

        final UserJson updateUserInfoResponse = nus.updateUser(jsonUser);

        step("Check that response contains ID (GUID)", () ->
                assertTrue(updateUserInfoResponse.getId().toString().matches(ID_REGEXP))
        );
        step("Check that response contains username", () ->
                assertEquals(user.getUsername(), updateUserInfoResponse.getUsername())
        );
        step("Check that response contains updated currency (KZT)", () ->
                assertEquals(CurrencyValues.KZT, updateUserInfoResponse.getCurrency())
        );
        step("Check that response contains updated firstname (Pizzly)", () ->
                assertEquals(firstName, updateUserInfoResponse.getFirstname())
        );
        step("Check that response contains updated surname (Pizzlyvich)", () ->
                assertEquals(secondName, updateUserInfoResponse.getSurname())
        );
    }

    @Test
    @DisplayName("REST: Список всех пользователей системы не должен быть пустым")
    @AllureId("200003")
    @Tag("REST")
    @GenerateUser()
    void allUsersTest(@User(selector = METHOD) UserJson user) throws Exception {
        final List<UserJson> allUsersResponse = nus.allUsers(user.getUsername());

        step("Check that all users list is not empty", () ->
                assertFalse(allUsersResponse.isEmpty())
        );
    }
}
