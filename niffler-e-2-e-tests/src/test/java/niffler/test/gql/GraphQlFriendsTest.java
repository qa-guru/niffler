package niffler.test.gql;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.AllureId;
import niffler.graphql.GraphQLClient;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.User;
import niffler.model.UserJson;
import niffler.test.gql.model.UserGql;
import niffler.test.gql.model.UserDataGql;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static niffler.jupiter.extension.CreateUserExtension.Selector.METHOD;

public class GraphQlFriendsTest extends BaseGraphQlTest {

    private static final ObjectMapper om = new ObjectMapper();
    private final ClassLoader cl = GraphQlFriendsTest.class.getClassLoader();

    private final GraphQLClient gqlClient = new GraphQLClient();

    @Test
    @DisplayName("GraphQL: Для нового пользователя должен возвращаться пустой список friends и invitations из niffler-gateway")
    @AllureId("400004")
    @GenerateUser
    void getFriendsTest(@User(selector = METHOD) UserJson user) throws Exception {
        apiLogin(user.getUsername(), user.getPassword());

        try (InputStream is = cl.getResourceAsStream("gql/getFriendsQuery.json")) {
            JsonNode query = om.readValue(is, JsonNode.class);
            UserDataGql response = gqlClient.friends(query);

            final List<UserGql> friends = response.getData().getUser().getFriends();
            final List<UserGql> invitations = response.getData().getUser().getInvitations();

            Assertions.assertTrue(friends.isEmpty());
            Assertions.assertTrue(invitations.isEmpty());
        }
    }
}
