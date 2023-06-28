package guru.qa.niffler.test.gql;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.gql.GraphQLClient;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.GqlReq;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.gql.UserDataGql;
import guru.qa.niffler.model.gql.UserGql;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static guru.qa.niffler.jupiter.extension.CreateUserExtension.Selector.METHOD;
import static io.qameta.allure.Allure.step;
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
