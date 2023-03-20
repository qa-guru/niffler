package niffler.test.graphql;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.qameta.allure.AllureId;
import niffler.graphql.GraphQLClient;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.User;
import niffler.model.UserJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static niffler.jupiter.extension.CreateUserExtension.Selector.METHOD;

public class GraphQlFriendsTest extends GraphQlTest {

    private static final ObjectMapper om = new ObjectMapper();
    private final ClassLoader cl = GraphQlFriendsTest.class.getClassLoader();

    private final GraphQLClient gqlClient = new GraphQLClient();

    @AllureId("40000")
    @Test
    @GenerateUser
    void getFriendsTest(@User(selector = METHOD) UserJson user) throws Exception {
        apiLogin(user.getUserName(), user.getPassword());

        try (InputStream is = cl.getResourceAsStream("gql/getFriendsQuery.json")) {
            JsonNode query = om.readValue(is, JsonNode.class);
            JsonNode response = gqlClient.request(query);
            JsonNode data = response.get("data");
            Assertions.assertNotNull(data);
            JsonNode usr = data.get("user");
            Assertions.assertNotNull(usr);
            ArrayNode friends = usr.withArray("friends");
            Assertions.assertTrue(friends.isEmpty());
            ArrayNode invitations = usr.withArray("invitations");
            Assertions.assertTrue(invitations.isEmpty());
        }
    }
}
