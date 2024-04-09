package guru.qa.niffler.test.gql;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.GenerateUsers;
import guru.qa.niffler.jupiter.annotation.GqlReq;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.gql.GqlRequest;
import guru.qa.niffler.model.gql.UpdateUserDataGql;
import guru.qa.niffler.model.gql.UserDataGql;
import guru.qa.niffler.model.gql.UserGql;
import guru.qa.niffler.model.gql.UsersDataGql;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("[GraphQL][niffler-gateway]: Пользователи")
@DisplayName("[GraphQL][niffler-gateway]: Пользователи")
public class GraphQlUsersTest extends BaseGraphQlTest {

    @Test
    @DisplayName("GraphQL: Для нового пользователя должна возвращаться информация из niffler-gateway c дефолтными значениями")
    @AllureId("400001")
    @Tag("GraphQL")
    @ApiLogin(user = @GenerateUser)
    void currentUserInfoShouldReceived(@User UserJson user,
                                       @Token String bearerToken,
                                       @GqlReq("gql/currentUserQuery.json") GqlRequest query) throws Exception {
        final UserDataGql currentUserResponse = gqlClient.currentUser(bearerToken, query);
        final UserGql userGql = currentUserResponse.getData().getUser();

        step("Check that response contains ID (GUID)", () ->
                assertTrue(userGql.getId().toString().matches(ID_REGEXP))
        );
        step("Check that response contains username", () ->
                assertEquals(user.username(), userGql.getUsername())
        );
        step("Check that response contains default currency (RUB)", () ->
                assertEquals(CurrencyValues.RUB, userGql.getCurrency())
        );
    }

    @Test
    @DisplayName("GraphQL: При обновлении юзера должны сохраняться значения в niffler-userdata")
    @AllureId("400002")
    @Tag("GraphQL")
    @ApiLogin(user = @GenerateUser)
    void updatedUserInfoShouldReceived(@User UserJson user,
                                       @Token String bearerToken,
                                       @GqlReq("gql/updateUserQuery.json") GqlRequest query) throws Exception {
        final UpdateUserDataGql updateUserResponse = gqlClient.updateUser(bearerToken, query);
        final UserGql userGql = updateUserResponse.getData().getUpdateUser();

        step("Check that response contains ID (GUID)", () ->
                assertTrue(userGql.getId().toString().matches(ID_REGEXP))
        );
        step("Check that response contains username", () ->
                assertEquals(user.username(), userGql.getUsername())
        );
        step("Check that response contains updated currency (EUR)", () ->
                assertEquals(CurrencyValues.EUR, userGql.getCurrency())
        );
        step("Check that response contains updated firstname (Pizzly)", () ->
                assertEquals("Pizzly", userGql.getFirstname())
        );
        step("Check that response contains updated surname (Pizzlyvich)", () ->
                assertEquals("Pizzlyvich", userGql.getSurname())
        );
    }

    @Test
    @DisplayName("GraphQL: Список всех пользователей системы не должен быть пустым")
    @AllureId("400003")
    @Tag("GraphQL")
    @ApiLogin(user = @GenerateUser)
    @GenerateUsers({
            @GenerateUser
    })
    void notEmptyUsersListShouldReceived(@Token String bearerToken,
                                         @GqlReq("gql/usersQuery.json") GqlRequest query) throws Exception {
        final UsersDataGql usersDataGql = gqlClient.allUsers(bearerToken, query);
        final List<UserGql> userGql = usersDataGql.getData().getUsers();

        step("Check that all users list is not empty", () ->
                assertFalse(userGql.isEmpty())
        );
    }
}
