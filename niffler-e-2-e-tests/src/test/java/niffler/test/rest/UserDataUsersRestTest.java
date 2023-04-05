package niffler.test.rest;

import io.qameta.allure.AllureId;
import niffler.api.NifflerUserdataClient;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.User;
import niffler.model.rest.CurrencyValues;
import niffler.model.rest.UserJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static niffler.jupiter.extension.CreateUserExtension.Selector.METHOD;

public class UserDataUsersRestTest extends BaseRestTest {

    private static final String ID_REGEXP = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    private final NifflerUserdataClient nus = new NifflerUserdataClient();

    @Test
    @DisplayName("REST: Для нового пользователя долна возвращаться информация из niffler-userdata c дефолтными значениями")
    @AllureId("200001")
    @Tag("REST")
    @GenerateUser()
    void currentUserTest(@User(selector = METHOD) UserJson user) throws Exception {
        UserJson currentUserResponse = nus.getCurrentUser(user.getUsername());

        Assertions.assertTrue(currentUserResponse.getId().toString().matches(ID_REGEXP));
        Assertions.assertEquals(user.getUsername(), currentUserResponse.getUsername());
        Assertions.assertEquals(CurrencyValues.RUB, currentUserResponse.getCurrency());
    }

    @Test
    @DisplayName("REST: При обновлении юзера должны сохраняться значения в niffler-userdata")
    @AllureId("200002")
    @Tag("REST")
    @GenerateUser()
    void updateUserTest(@User(selector = METHOD) UserJson user) throws Exception {
        final String firstName = "FirstName";
        final String secondName = "SecondName";

        UserJson jsonUser = new UserJson();
        jsonUser.setUsername(user.getUsername());
        jsonUser.setCurrency(CurrencyValues.EUR);
        jsonUser.setFirstname(firstName);
        jsonUser.setSurname(secondName);

        UserJson updateUserInfoResponse = nus.updateUser(jsonUser);

        Assertions.assertTrue(updateUserInfoResponse.getId().toString().matches(ID_REGEXP));
        Assertions.assertEquals(user.getUsername(), updateUserInfoResponse.getUsername());
        Assertions.assertEquals(CurrencyValues.EUR, updateUserInfoResponse.getCurrency());
        Assertions.assertEquals(firstName, updateUserInfoResponse.getFirstname());
        Assertions.assertEquals(secondName, updateUserInfoResponse.getSurname());
    }

    @Test
    @DisplayName("REST: Список всех пользователей системы не должен быть пустым")
    @AllureId("200003")
    @Tag("REST")
    @GenerateUser()
    void allUsersTest(@User(selector = METHOD) UserJson user) throws Exception {
        List<UserJson> allUsersResponse = nus.allUsers(user.getUsername());

        Assertions.assertFalse(allUsersResponse.isEmpty());
    }
}
