package niffler.test.gql;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.AllureId;
import niffler.graphql.GraphQLClient;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.User;
import niffler.model.CurrencyValues;
import niffler.model.UserJson;
import niffler.test.gql.model.UpdateUserDataGql;
import niffler.test.gql.model.UserDataGql;
import niffler.test.gql.model.UserGql;
import niffler.test.gql.model.UsersDataGql;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static niffler.jupiter.extension.CreateUserExtension.Selector.METHOD;

public class GraphQlUsersTest extends BaseGraphQlTest {

    private static final String ID_REGEXP = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    private static final ObjectMapper om = new ObjectMapper();
    private final ClassLoader cl = GraphQlUsersTest.class.getClassLoader();

    private final GraphQLClient gqlClient = new GraphQLClient();

    @Test
    @DisplayName("GraphQL: Для нового пользователя долна возвращаться информация из niffler-gateway c дефолтными значениями")
    @AllureId("400001")
    @GenerateUser
    void currentUserTest(@User(selector = METHOD) UserJson user) throws Exception {
        apiLogin(user.getUsername(), user.getPassword());

        try (InputStream is = cl.getResourceAsStream("gql/currentUserQuery.json")) {
            JsonNode query = om.readValue(is, JsonNode.class);
            UserDataGql currentUserResponse = gqlClient.currentUser(query);

            final UserGql userGql = currentUserResponse.getData().getUser();

            Assertions.assertTrue(userGql.getId().toString().matches(ID_REGEXP));
            Assertions.assertEquals(user.getUsername(), userGql.getUsername());
            Assertions.assertEquals(CurrencyValues.RUB, userGql.getCurrency());
        }
    }

    @Test
    @DisplayName("GraphQL: При обновлении юзера должны сохраняться значения в niffler-gateway")
    @AllureId("400002")
    @GenerateUser()
    void updateUserTest(@User(selector = METHOD) UserJson user) throws Exception {
        apiLogin(user.getUsername(), user.getPassword());

        try (InputStream is = cl.getResourceAsStream("gql/updateUserQuery.json")) {
            JsonNode query = om.readValue(is, JsonNode.class);
            UpdateUserDataGql updateUserResponse = gqlClient.updateUser(query);

            final UserGql userGql = updateUserResponse.getData().getUpdateUser();

            Assertions.assertTrue(userGql.getId().toString().matches(ID_REGEXP));
            Assertions.assertEquals(user.getUsername(), userGql.getUsername());
            Assertions.assertEquals(CurrencyValues.EUR, userGql.getCurrency());
            Assertions.assertEquals("Pizzly", userGql.getFirstname());
            Assertions.assertEquals("Pizzlyvich", userGql.getSurname());
        }
    }

    @Test
    @DisplayName("GraphQL: Список всех пользователей системы не должен быть пустым")
    @AllureId("400003")
    @GenerateUser()
    void allUsersTest(@User(selector = METHOD) UserJson user) throws Exception {
        apiLogin(user.getUsername(), user.getPassword());

        try (InputStream is = cl.getResourceAsStream("gql/usersQuery.json")) {
            JsonNode query = om.readValue(is, JsonNode.class);
            UsersDataGql usersDataGql = gqlClient.allUsers(query);

            final List<UserGql> userGql = usersDataGql.getData().getUsers();

            Assertions.assertFalse(userGql.isEmpty());
        }
    }
}
