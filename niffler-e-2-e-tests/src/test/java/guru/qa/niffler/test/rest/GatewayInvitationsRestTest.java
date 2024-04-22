package guru.qa.niffler.test.rest;

import guru.qa.niffler.api.UserdataApiClient;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Friends;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.GenerateUsers;
import guru.qa.niffler.jupiter.annotation.IncomeInvitations;
import guru.qa.niffler.jupiter.annotation.OutcomeInvitations;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.FriendJson;
import guru.qa.niffler.model.rest.FriendState;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static guru.qa.niffler.jupiter.annotation.User.Selector.METHOD;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("[REST][niffler-gateway]: Приглашения")
@DisplayName("[REST][niffler-gateway]: Приглашения")
public class GatewayInvitationsRestTest extends BaseRestTest {

    private static final UserdataApiClient userdataApiClient = new UserdataApiClient();

    @Test
    @DisplayName("REST: Для пользователя должен возвращаться список исходящих инвайтов из niffler-userdata")
    @AllureId("200015")
    @Tag("REST")
    @ApiLogin(
            user = @GenerateUser(
                    friends = @Friends(count = 1),
                    outcomeInvitations = @OutcomeInvitations(count = 1)
            )
    )
    void getAllOutcomeInvitationsListTest(@User UserJson user,
                                          @Token String bearerToken) throws Exception {
        UserJson testOutInvitation = user.testData().outcomeInvitations().get(0);

        final List<UserJson> outcomeInvitations = gatewayApiClient.outcomeInvitations(bearerToken, null);
        step("Check that response not null", () ->
                assertNotNull(outcomeInvitations)
        );
        step("Check response list size", () ->
                assertEquals(1, outcomeInvitations.size())
        );

        final var foundedInvitation = outcomeInvitations.get(0);

        step("Check outcome invitation in response", () -> {
            assertSame(FriendState.INVITE_SENT, foundedInvitation.friendState());
            assertEquals(testOutInvitation.id(), foundedInvitation.id());
            assertEquals(testOutInvitation.username(), foundedInvitation.username());
        });
    }

    @Test
    @DisplayName("REST: Для пользователя должен возвращаться список исходящих инвайтов из niffler-userdata " +
            "с фильтраций по username, если передан searchQuery")
    @AllureId("200016")
    @Tag("REST")
    @ApiLogin(
            user = @GenerateUser(
                    friends = @Friends(count = 1),
                    outcomeInvitations = @OutcomeInvitations(count = 2)
            )
    )
    void getFilteredOutcomeInvitationsListTest(@User UserJson user,
                                               @Token String bearerToken) throws Exception {
        UserJson testOutInvitation = user.testData().outcomeInvitations().get(0);

        final List<UserJson> outcomeInvitations = gatewayApiClient.outcomeInvitations(bearerToken, testOutInvitation.username());
        step("Check that response not null", () ->
                assertNotNull(outcomeInvitations)
        );
        step("Check response list size", () ->
                assertEquals(1, outcomeInvitations.size())
        );

        final var foundedInvitation = outcomeInvitations.get(0);

        step("Check outcome invitation in response", () -> {
            assertSame(FriendState.INVITE_SENT, foundedInvitation.friendState());
            assertEquals(testOutInvitation.id(), foundedInvitation.id());
            assertEquals(testOutInvitation.username(), foundedInvitation.username());
        });
    }

    @Test
    @DisplayName("REST: Для пользователя должен возвращаться список приглашений дружить из niffler-userdata")
    @AllureId("200017")
    @Tag("REST")
    @ApiLogin(
            user = @GenerateUser(
                    incomeInvitations = @IncomeInvitations(count = 1)
            )
    )
    void getAllIncomeInvitationsListTest(@User UserJson user,
                                         @Token String bearerToken) throws Exception {
        UserJson testInvitation = user.testData().incomeInvitations().get(0);
        final List<UserJson> invitations = gatewayApiClient.incomeInvitations(bearerToken, null);
        step("Check that response not null", () ->
                assertNotNull(invitations)
        );
        step("Check that response contains expected invitations", () ->
                assertEquals(1, invitations.size())
        );

        final var invitation = invitations.get(0);

        step("Check invitation in response", () -> {
            assertSame(FriendState.INVITE_RECEIVED, invitation.friendState());
            assertEquals(testInvitation.username(), invitation.username());
        });
    }

    @Test
    @DisplayName("REST: Для пользователя должен возвращаться список приглашений дружить из niffler-userdata " +
            "с фильтраций по username, если передан searchQuery")
    @AllureId("200018")
    @Tag("REST")
    @ApiLogin(
            user = @GenerateUser(
                    incomeInvitations = @IncomeInvitations(count = 2)
            )
    )
    void getFilteredIncomeInvitationsListTest(@User UserJson user,
                                              @Token String bearerToken) throws Exception {
        UserJson testInvitation = user.testData().incomeInvitations().get(0);
        final List<UserJson> invitations = gatewayApiClient.incomeInvitations(bearerToken, testInvitation.username());
        step("Check that response not null", () ->
                assertNotNull(invitations)
        );
        step("Check that response contains expected invitations", () ->
                assertEquals(1, invitations.size())
        );

        final var invitation = invitations.get(0);

        step("Check invitation in response", () -> {
            assertSame(FriendState.INVITE_RECEIVED, invitation.friendState());
            assertEquals(testInvitation.username(), invitation.username());
        });
    }

    @Test
    @DisplayName("REST: Прием заявки в друзья")
    @AllureId("200019")
    @Tag("REST")
    @ApiLogin(
            user = @GenerateUser(
                    incomeInvitations = @IncomeInvitations(count = 1)
            )
    )
    void acceptInvitationTest(@User UserJson user,
                              @Token String bearerToken) throws Exception {
        final String incomeInvitation = user.testData().incomeInvitations().get(0).username();

        final UserJson friend = gatewayApiClient.acceptInvitation(bearerToken, new FriendJson(
                incomeInvitation
        ));
        step("Check that response not null", () ->
                assertNotNull(friend)
        );

        step("Check friend in response", () -> {
            assertEquals(user.testData().incomeInvitations().get(0).username(), friend.username());
            assertEquals(FriendState.FRIEND, friend.friendState());
        });

        step("Check that friends present in GET /friends request for both users", () ->
                Assertions.assertAll(
                        () -> assertEquals(
                                1,
                                gatewayApiClient.allFriends(bearerToken, null).size(),
                                "Current user should have friend after accepting"
                        ),
                        () -> assertEquals(
                                1,
                                userdataApiClient.friends(incomeInvitation, null).size(),
                                "Target friend should have friend after accepting"
                        )
                )
        );
    }

    @Test
    @DisplayName("REST: Отклонение заявки в друзья")
    @AllureId("200020")
    @Tag("REST")
    @ApiLogin(
            user = @GenerateUser(
                    incomeInvitations = @IncomeInvitations(count = 1)
            )
    )
    void declineInvitationTest(@User UserJson user,
                               @Token String bearerToken) throws Exception {
        final String incomeInvitation = user.testData().incomeInvitations().get(0).username();

        final UserJson declinedFriend = gatewayApiClient.declineInvitation(bearerToken, new FriendJson(
                incomeInvitation
        ));
        step("Check that response not null", () ->
                assertNotNull(declinedFriend)
        );

        step("Check declined friend in response", () -> {
            assertEquals(user.testData().incomeInvitations().get(0).username(), declinedFriend.username());
            assertNull(declinedFriend.friendState());
        });

        step("Check that outcome & income invitations removed for both users", () ->
                Assertions.assertAll(
                        () -> assertTrue(
                                gatewayApiClient.incomeInvitations(bearerToken, null).isEmpty(),
                                "Current user should not have income invitations after declining"),
                        () -> assertTrue(
                                userdataApiClient.outcomeInvitations(incomeInvitation, null).isEmpty(),
                                "Inviter should not have outcome invitations after declining"
                        )
                )
        );
    }

    @Test
    @DisplayName("REST: Отправка приглашения дружить")
    @AllureId("200021")
    @Tag("REST")
    @GenerateUsers({
            @GenerateUser
    })
    @ApiLogin(user = @GenerateUser)
    void sendInvitationTest(@User(selector = METHOD) UserJson friend,
                            @Token String bearerToken) throws Exception {
        final String friendWillBeAdded = friend.username();

        final UserJson outcomeInvitation = gatewayApiClient.sendInvitation(bearerToken, new FriendJson(
                friendWillBeAdded
        ));
        step("Check that response not null", () ->
                assertNotNull(outcomeInvitation)
        );

        step("Check invitation in response", () -> {
            assertEquals(friendWillBeAdded, outcomeInvitation.username());
            assertEquals(FriendState.INVITE_SENT, outcomeInvitation.friendState());
        });

        step("Check that friends request & income invitation present for both users", () ->
                Assertions.assertAll(
                        () -> assertEquals(
                                1,
                                gatewayApiClient.outcomeInvitations(bearerToken, null).size(),
                                "Current user should have pending friend after adding"),
                        () -> assertEquals(
                                1,
                                userdataApiClient.incomeInvitations(friend.username(), null).size(),
                                "Target friend should have 1 invitation"
                        )
                )
        );
    }
}
