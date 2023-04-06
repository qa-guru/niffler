package niffler.test.gql;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import niffler.gql.GraphQLClient;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.User;
import niffler.model.gql.UserDataGql;
import niffler.model.gql.UserGql;
import niffler.model.rest.UserJson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static io.qameta.allure.Allure.step;
import static niffler.jupiter.extension.CreateUserExtension.Selector.METHOD;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("[GraphQL][niffler-gateway]: Друзья")
@DisplayName("[GraphQL][niffler-gateway]: Друзья")
public class GraphQlFriendsTest extends BaseGraphQlTest {

    private static final ObjectMapper om = new ObjectMapper();
    private final ClassLoader cl = GraphQlFriendsTest.class.getClassLoader();

    private final GraphQLClient gqlClient = new GraphQLClient();

    @Test
    @DisplayName("GraphQL: Для нового пользователя должен возвращаться пустой список friends и invitations из niffler-gateway")
    @AllureId("400004")
    @Tag("GraphQL")
    @GenerateUser
    void getFriendsTest(@User(selector = METHOD) UserJson user) throws Exception {
        apiLogin(user.getUsername(), user.getPassword());

        try (InputStream is = cl.getResourceAsStream("gql/getFriendsQuery.json")) {
            JsonNode query = om.readValue(is, JsonNode.class);
            UserDataGql response = gqlClient.friends(query);

            final List<UserGql> friends = response.getData().getUser().getFriends();
            final List<UserGql> invitations = response.getData().getUser().getInvitations();

            step("Check that friends list is empty", () ->
                    assertTrue(friends.isEmpty())
            );
            step("Check that invitations list is empty", () ->
                    assertTrue(invitations.isEmpty())
            );
        }
    }
}
