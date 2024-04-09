package guru.qa.niffler.test.gql;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Friends;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.GqlReq;
import guru.qa.niffler.jupiter.annotation.IncomeInvitations;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.gql.GqlRequest;
import guru.qa.niffler.model.gql.UserDataGql;
import guru.qa.niffler.model.gql.UserGql;
import guru.qa.niffler.model.rest.FriendState;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("[GraphQL][niffler-gateway]: Друзья")
@DisplayName("[GraphQL][niffler-gateway]: Друзья")
public class GraphQlFriendsTest extends BaseGraphQlTest {

    @Test
    @DisplayName("GraphQL: Для нового пользователя должен возвращаться пустой список friends и invitations из niffler-gateway")
    @AllureId("400004")
    @Tag("GraphQL")
    @ApiLogin(user = @GenerateUser)
    void emptyFriendsAndInvitationsListShouldReceivedForNewUser(@Token String bearerToken,
                                                                @GqlReq("gql/getFriendsQuery.json") GqlRequest query) throws Exception {
        final UserDataGql response = gqlClient.friends(bearerToken, query);

        final List<UserGql> friends = response.getData().getUser().getFriends();
        final List<UserGql> invitations = response.getData().getUser().getInvitations();

        step("Check that friends list is empty", () ->
                assertTrue(friends.isEmpty())
        );
        step("Check that invitations list is empty", () ->
                assertTrue(invitations.isEmpty())
        );
    }

    @CsvSource({
            "friends, gql/getFriends2FriedsSubQuery.json",
            "invitations, gql/getFriends2InvitationsSubQuery.json"
    })
    @ParameterizedTest(name = "Получена ошибка Can`t fetch over 2 {0} sub-queries")
    @DisplayName("GraphQL: Невозможно получить более 2-х уровней вложенности запросов")
    @AllureId("400005")
    @Tag("GraphQL")
    @ApiLogin(
            user = @GenerateUser(
                    friends = @Friends(count = 2),
                    incomeInvitations = @IncomeInvitations(count = 2)
            )
    )
    void errorShouldReceivedForOver2SubQueries(String expectedMessagePart,
                                               @GqlReq GqlRequest query,
                                               @Token String bearerToken) throws Exception {
        final UserDataGql response = gqlClient.friends(bearerToken, query);

        step("Check error message", () ->
                assertEquals(
                        "Can`t fetch over 2 " + expectedMessagePart + " sub-queries",
                        response.getErrors().get(0).message()
                )
        );
    }

    @Test
    @DisplayName("GraphQL: Для пользователя должен возвращаться список друзей " +
            "и исходящих запросов на дружбу из niffler-userdata")
    @AllureId("400006")
    @Tag("GraphQL")
    @ApiLogin(
            user = @GenerateUser(
                    friends = @Friends(count = 1),
                    incomeInvitations = @IncomeInvitations(count = 1)
            )
    )
    void friendsAndIncomeInvitationsListShouldReceived(@GqlReq("gql/getFriendsQuery.json") GqlRequest query,
                                                       @User UserJson user,
                                                       @Token String bearerToken) throws Exception {
        UserJson friend = user.testData().friends().get(0);
        UserJson invitation = user.testData().incomeInvitations().get(0);

        final UserDataGql response = gqlClient.friends(bearerToken, query);

        step("Check friend in response", () -> {
            assertEquals(1, response.getData().getUser().getFriends().size());
            final UserGql friendResp = response.getData().getUser().getFriends().get(0);

            assertEquals(friend.id(), friendResp.getId());
            assertEquals(friend.username(), friendResp.getUsername());
            assertEquals(FriendState.FRIEND, friendResp.getFriendState());
        });
        step("Check income invitation in response", () -> {
            assertEquals(1, response.getData().getUser().getInvitations().size());
            final UserGql invResp = response.getData().getUser().getInvitations().get(0);

            assertEquals(invitation.id(), invResp.getId());
            assertEquals(invitation.username(), invResp.getUsername());
            assertEquals(FriendState.INVITE_RECEIVED, invResp.getFriendState());
        });
    }
}
