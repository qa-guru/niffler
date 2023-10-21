package guru.qa.niffler.test.gql;

import guru.qa.niffler.gql.GatewayGqlClient;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.GqlReq;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.model.gql.GqlRequest;
import guru.qa.niffler.model.gql.UserDataGql;
import guru.qa.niffler.model.gql.UserGql;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("[GraphQL][niffler-gateway]: Друзья")
@DisplayName("[GraphQL][niffler-gateway]: Друзья")
public class GraphQlFriendsTest extends BaseGraphQlTest {

    private final GatewayGqlClient gqlClient = new GatewayGqlClient();

    @Test
    @DisplayName("GraphQL: Для нового пользователя должен возвращаться пустой список friends и invitations из niffler-gateway")
    @AllureId("400004")
    @Tag("GraphQL")
    @ApiLogin(user = @GenerateUser)
    void getFriendsTest(@Token String bearerToken,
                        @GqlReq("gql/getFriendsQuery.json") GqlRequest query) throws Exception {

        UserDataGql response = gqlClient.friends(bearerToken, query);

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
