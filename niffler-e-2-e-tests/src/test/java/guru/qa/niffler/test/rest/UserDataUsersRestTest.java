package guru.qa.niffler.test.rest;

import guru.qa.niffler.api.NifflerUserdataClient;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

import static guru.qa.niffler.jupiter.annotation.User.Selector.METHOD;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("[REST][niffler-userdata]: Пользователи")
@DisplayName("[REST][niffler-userdata]: Пользователи")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDataUsersRestTest extends BaseRestTest {

    private static final String ID_REGEXP = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    private final NifflerUserdataClient nus = new NifflerUserdataClient();

    @Test
    @DisplayName("REST: Для нового пользователя должна возвращаться информация из niffler-userdata c дефолтными значениями")
    @AllureId("200001")
    @Tag("REST")
    @GenerateUser()
    @Order(1)
    void currentUserTest(@User(selector = METHOD) UserJson user) throws Exception {
        final UserJson currentUserResponse = nus.getCurrentUser(user.username());

        step("Check that response contains ID (GUID)", () ->
                assertTrue(currentUserResponse.id().toString().matches(ID_REGEXP))
        );
        step("Check that response contains username", () ->
                assertEquals(user.username(), currentUserResponse.username())
        );
        step("Check that response contains default currency (RUB)", () ->
                assertEquals(CurrencyValues.RUB, currentUserResponse.currency())
        );
    }

    @Test
    @DisplayName("REST: При обновлении юзера должны сохраняться значения в niffler-userdata")
    @AllureId("200002")
    @Tag("REST")
    @GenerateUser()
    @Order(2)
    void updateUserTest(@User(selector = METHOD) UserJson user) throws Exception {
        final String firstName = "Pizzly";
        final String secondName = "Pizzlyvich";

        UserJson jsonUser = new UserJson(
                null,
                user.username(),
                firstName,
                secondName,
                CurrencyValues.KZT,
                null,
                null,
                null
        );

        final UserJson updateUserInfoResponse = nus.updateUser(jsonUser);

        step("Check that response contains ID (GUID)", () ->
                assertTrue(updateUserInfoResponse.id().toString().matches(ID_REGEXP))
        );
        step("Check that response contains username", () ->
                assertEquals(user.username(), updateUserInfoResponse.username())
        );
        step("Check that response contains updated currency (KZT)", () ->
                assertEquals(CurrencyValues.KZT, updateUserInfoResponse.currency())
        );
        step("Check that response contains updated firstname (Pizzly)", () ->
                assertEquals(firstName, updateUserInfoResponse.firstname())
        );
        step("Check that response contains updated surname (Pizzlyvich)", () ->
                assertEquals(secondName, updateUserInfoResponse.surname())
        );
    }

    @Test
    @DisplayName("REST: Список всех пользователей системы не должен быть пустым")
    @AllureId("200003")
    @Tag("REST")
    @GenerateUser()
    @Order(3)
    void allUsersTest(@User(selector = METHOD) UserJson user) throws Exception {
        final List<UserJson> allUsersResponse = nus.allUsers(user.username());

        step("Check that all users list is not empty", () ->
                assertFalse(allUsersResponse.isEmpty())
        );
    }
}
