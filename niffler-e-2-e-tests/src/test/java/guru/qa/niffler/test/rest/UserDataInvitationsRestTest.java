package guru.qa.niffler.test.rest;

import guru.qa.niffler.api.UserdataApiClient;
import guru.qa.niffler.jupiter.annotation.Friends;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.GenerateUsers;
import guru.qa.niffler.jupiter.annotation.IncomeInvitations;
import guru.qa.niffler.jupiter.annotation.OutcomeInvitations;
import guru.qa.niffler.jupiter.annotation.User;
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

@Epic("[REST][niffler-userdata]: Друзья")
@DisplayName("[REST][niffler-userdata]: Друзья")
public class UserDataInvitationsRestTest extends BaseRestTest {

    private static final UserdataApiClient userdataClient = new UserdataApiClient();

    @Test
    @DisplayName("REST: Для пользователя должен возвращаться список исходящих инвайтов из niffler-userdata")
    @AllureId("200007")
    @Tag("REST")
    @GenerateUser(
            friends = @Friends(count = 1),
            outcomeInvitations = @OutcomeInvitations(count = 1)
    )
    void getAllOutcomeInvitationsListTest(@User(selector = METHOD) UserJson user) throws Exception {
        UserJson testOutInvitation = user.testData().outcomeInvitations().get(0);

        final List<UserJson> outcomeInvitations = userdataClient.outcomeInvitations(user.username(), null);
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
    @AllureId("200008")
    @Tag("REST")
    @GenerateUser(
            friends = @Friends(count = 1),
            outcomeInvitations = @OutcomeInvitations(count = 2)
    )
    void getFilteredOutcomeInvitationsListTest(@User(selector = METHOD) UserJson user) throws Exception {
        UserJson testOutInvitation = user.testData().outcomeInvitations().get(0);

        final List<UserJson> outcomeInvitations = userdataClient.outcomeInvitations(user.username(), testOutInvitation.username());
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
    @AllureId("200009")
    @Tag("REST")
    @GenerateUser(
            incomeInvitations = @IncomeInvitations(count = 1)
    )
    void getAllIncomeInvitationsListTest(@User(selector = METHOD) UserJson user) throws Exception {
        UserJson testInvitation = user.testData().incomeInvitations().get(0);
        final List<UserJson> invitations = userdataClient.incomeInvitations(user.username(), null);
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
    @AllureId("200010")
    @Tag("REST")
    @GenerateUser(
            incomeInvitations = @IncomeInvitations(count = 2)
    )
    void getFilteredIncomeInvitationsListTest(@User(selector = METHOD) UserJson user) throws Exception {
        UserJson testInvitation = user.testData().incomeInvitations().get(0);
        final List<UserJson> invitations = userdataClient.incomeInvitations(user.username(), testInvitation.username());
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
    @AllureId("200011")
    @Tag("REST")
    @GenerateUser(
            incomeInvitations = @IncomeInvitations(count = 1)
    )
    void acceptInvitationTest(@User(selector = METHOD) UserJson user) throws Exception {
        final String currentUser = user.username();
        final String incomeInvitation = user.testData().incomeInvitations().get(0).username();

        final UserJson friend = userdataClient.acceptInvitation(currentUser, incomeInvitation);
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
                                userdataClient.friends(currentUser, null).size(),
                                "Current user should have friend after accepting"
                        ),
                        () -> assertEquals(
                                1,
                                userdataClient.friends(incomeInvitation, null).size(),
                                "Target friend should have friend after accepting"
                        )
                )
        );
    }

    @Test
    @DisplayName("REST: Отклонение заявки в друзья")
    @AllureId("200012")
    @Tag("REST")
    @GenerateUser(
            incomeInvitations = @IncomeInvitations(count = 1)
    )
    void declineInvitationTest(@User(selector = METHOD) UserJson user) throws Exception {
        final String currentUser = user.username();
        final String incomeInvitation = user.testData().incomeInvitations().get(0).username();

        final UserJson declinedFriend = userdataClient.declineInvitation(currentUser, incomeInvitation);
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
                                userdataClient.incomeInvitations(currentUser, null).isEmpty(),
                                "Current user should not have income invitations after declining"),
                        () -> assertTrue(
                                userdataClient.outcomeInvitations(incomeInvitation, null).isEmpty(),
                                "Inviter should not have outcome invitations after declining"
                        )
                )
        );
    }

    @Test
    @DisplayName("REST: Отправка приглашения дружить")
    @AllureId("200013")
    @Tag("REST")
    @GenerateUsers({
            @GenerateUser,
            @GenerateUser
    })
    void sendInvitationTest(@User(selector = METHOD) UserJson[] users) throws Exception {
        final String currentUser = users[0].username();
        final String friendWillBeAdded = users[1].username();

        final UserJson outcomeInvitation = userdataClient.sendInvitation(currentUser, friendWillBeAdded);
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
                                userdataClient.outcomeInvitations(currentUser, null).size(),
                                "Current user should have pending friend after adding"),
                        () -> assertEquals(
                                1,
                                userdataClient.incomeInvitations(friendWillBeAdded, null).size(),
                                "Target friend should have 1 invitation"
                        )
                )
        );
    }
}
