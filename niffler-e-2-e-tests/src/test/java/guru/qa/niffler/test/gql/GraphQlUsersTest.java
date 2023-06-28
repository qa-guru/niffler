package guru.qa.niffler.test.gql;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.gql.GraphQLClient;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.GenerateUsers;
import guru.qa.niffler.jupiter.annotation.GqlReq;
import guru.qa.niffler.jupiter.annotation.User;
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

import static guru.qa.niffler.jupiter.extension.CreateUserExtension.Selector.METHOD;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("[GraphQL][niffler-gateway]: Пользователи")
@DisplayName("[GraphQL][niffler-gateway]: Пользователи")
public class GraphQlUsersTest extends BaseGraphQlTest {

    private final GraphQLClient gqlClient = new GraphQLClient();

    @Test
    @DisplayName("GraphQL: Для нового пользователя должна возвращаться информация из niffler-gateway c дефолтными значениями")
    @AllureId("400001")
    @Tag("GraphQL")
    @GenerateUser
    void currentUserTest(@User(selector = METHOD) UserJson user,
                         @GqlReq("gql/currentUserQuery.json") JsonNode query) throws Exception {
        apiLogin(user.getUsername(), user.getPassword());

        UserDataGql currentUserResponse = gqlClient.currentUser(query);

        final UserGql userGql = currentUserResponse.getData().getUser();

        step("Check that response contains ID (GUID)", () ->
                assertTrue(userGql.getId().toString().matches(ID_REGEXP))
        );
        step("Check that response contains username", () ->
                assertEquals(user.getUsername(), userGql.getUsername())
        );
        step("Check that response contains default currency (RUB)", () ->
                assertEquals(CurrencyValues.RUB, userGql.getCurrency())
        );
    }

    @Test
    @DisplayName("GraphQL: При обновлении юзера должны сохраняться значения в niffler-gateway")
    @AllureId("400002")
    @Tag("GraphQL")
    @GenerateUser()
    void updateUserTest(@User(selector = METHOD) UserJson user,
                        @GqlReq("gql/updateUserQuery.json") JsonNode query) throws Exception {
        apiLogin(user.getUsername(), user.getPassword());

        UpdateUserDataGql updateUserResponse = gqlClient.updateUser(query);

        final UserGql userGql = updateUserResponse.getData().getUpdateUser();

        step("Check that response contains ID (GUID)", () ->
                assertTrue(userGql.getId().toString().matches(ID_REGEXP))
        );
        step("Check that response contains username", () ->
                assertEquals(user.getUsername(), userGql.getUsername())
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
    @GenerateUsers({
            @GenerateUser,
            @GenerateUser
    })
    void allUsersTest(@User(selector = METHOD) UserJson[] users,
                      @GqlReq("gql/usersQuery.json") JsonNode query) throws Exception {
        final UserJson currentUser = users[0];
        apiLogin(currentUser.getUsername(), currentUser.getPassword());

        UsersDataGql usersDataGql = gqlClient.allUsers(query);

        final List<UserGql> userGql = usersDataGql.getData().getUsers();

        step("Check that all users list is not empty", () ->
                assertFalse(userGql.isEmpty())
        );
    }
}
