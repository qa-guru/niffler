package niffler.test.gql;

import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import niffler.gql.GraphQLClient;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.GqlReq;
import niffler.jupiter.annotation.User;
import niffler.model.gql.UserDataGql;
import niffler.model.gql.UserGql;
import niffler.model.rest.UserJson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static niffler.jupiter.extension.CreateUserExtension.Selector.METHOD;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("[GraphQL][niffler-gateway]: Друзья")
@DisplayName("[GraphQL][niffler-gateway]: Друзья")
public class GraphQlFriendsTest extends BaseGraphQlTest {

    private final GraphQLClient gqlClient = new GraphQLClient();

    @Test
    @DisplayName("GraphQL: Для нового пользователя должен возвращаться пустой список friends и invitations из niffler-gateway")
    @AllureId("400004")
    @Tag("GraphQL")
    @GenerateUser
    void getFriendsTest(@User(selector = METHOD) UserJson user,
                        @GqlReq("gql/getFriendsQuery.json") JsonNode query) throws Exception {
        apiLogin(user.getUsername(), user.getPassword());

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
