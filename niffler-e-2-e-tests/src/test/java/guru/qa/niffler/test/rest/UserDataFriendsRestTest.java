package guru.qa.niffler.test.rest;

import guru.qa.niffler.api.NifflerUserdataClient;
import guru.qa.niffler.jupiter.annotation.Friends;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.GenerateUsers;
import guru.qa.niffler.jupiter.annotation.IncomeInvitations;
import guru.qa.niffler.jupiter.annotation.OutcomeInvitations;
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
import java.util.Optional;

import static guru.qa.niffler.jupiter.annotation.User.Selector.METHOD;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("[REST][niffler-userdata]: Друзья")
@DisplayName("[REST][niffler-userdata]: Друзья")
public class UserDataFriendsRestTest extends BaseRestTest {

    private final NifflerUserdataClient nus = new NifflerUserdataClient();

    @Test
    @DisplayName("REST: Для пользователя должен возвращаться список друзей из niffler-userdata" +
            " при передаче includePending = false")
    @AllureId("200004")
    @Tag("REST")
    @GenerateUser(
            friends = @Friends(count = 1),
            outcomeInvitations = @OutcomeInvitations(count = 1)
    )
    void getAllFriendsListWithoutInvitationTest(@User(selector = METHOD) UserJson user) throws Exception {
        final List<UserJson> friends = nus.friends(user.username(), false);

        step("Check that response contains expected users", () ->
                assertEquals(1, friends.size())
        );

        Optional<UserJson> friend = friends.stream()
                .filter(u -> u.friendState() == FriendState.FRIEND)
                .findFirst();

        Optional<UserJson> invitation = friends.stream()
                .filter(u -> u.friendState() == FriendState.INVITE_SENT)
                .findFirst();

        step("Check friend in response", () -> {
            assertTrue(friend.isPresent());
            assertEquals(user.testData().friendsJsons().get(0).username(), friend.get().username());
            assertEquals(FriendState.FRIEND, friend.get().friendState());
        });

        step("Check that no invitation present in response", () -> assertFalse(invitation.isPresent()));
    }

    @Test
    @DisplayName("REST: Для пользователя должен возвращаться список друзей и инвайтов из niffler-userdata" +
            " при передаче includePending = true")
    @AllureId("200005")
    @Tag("REST")
    @GenerateUser(
            friends = @Friends(count = 1),
            outcomeInvitations = @OutcomeInvitations(count = 1)
    )
    void getAllFriendsListWithInvitationTest(@User(selector = METHOD) UserJson user) throws Exception {
        final List<UserJson> friends = nus.friends(user.username(), true);

        step("Check that response contains expected users", () ->
                assertEquals(2, friends.size())
        );

        Optional<UserJson> friend = friends.stream()
                .filter(u -> u.friendState() == FriendState.FRIEND)
                .findFirst();

        Optional<UserJson> invitation = friends.stream()
                .filter(u -> u.friendState() == FriendState.INVITE_SENT)
                .findFirst();

        step("Check friend in response", () -> {
            assertTrue(friend.isPresent());
            assertEquals(user.testData().friendsJsons().get(0).username(), friend.get().username());
            assertEquals(FriendState.FRIEND, friend.get().friendState());
        });

        step("Check invitation in response", () -> {
            assertTrue(invitation.isPresent());
            assertEquals(user.testData().invitationsJsons().get(0).username(), invitation.get().username());
            assertEquals(FriendState.INVITE_SENT, invitation.get().friendState());
        });
    }

    @Test
    @DisplayName("REST: Для пользователя должен возвращаться список приглашений дружить из niffler-userdata")
    @AllureId("200006")
    @Tag("REST")
    @GenerateUser(
            incomeInvitations = @IncomeInvitations(count = 1)
    )
    void getInvitationTest(@User(selector = METHOD) UserJson user) throws Exception {
        final List<UserJson> invitations = nus.invitations(user.username());

        step("Check that response contains expected invitations", () ->
                assertEquals(1, invitations.size())
        );

        UserJson invitation = invitations.get(0);

        step("Check invitation in response", () -> {
            assertEquals(user.testData().invitationsJsons().get(0).username(), invitation.username());
            assertEquals(FriendState.INVITE_RECEIVED, invitation.friendState());
        });
    }

    @Test
    @DisplayName("REST: Прием заявки в друзья")
    @AllureId("200007")
    @Tag("REST")
    @GenerateUser(
            incomeInvitations = @IncomeInvitations(count = 1)
    )
    void acceptInvitationTest(@User(selector = METHOD) UserJson user) throws Exception {
        final String currentUser = user.username();
        final String incomeInvitation = user.testData().invitationsJsons().get(0).username();

        FriendJson fj = new FriendJson(incomeInvitation);
        final List<UserJson> friends = nus.acceptInvitation(currentUser, fj);
        UserJson friend = friends.get(0);

        step("Check friend in response", () -> {
            assertEquals(user.testData().invitationsJsons().get(0).username(), friend.username());
            assertEquals(FriendState.FRIEND, friend.friendState());
        });

        step("Check that friends present in GET /friends request for both users", () ->
                Assertions.assertAll(
                        () -> assertEquals(
                                1,
                                nus.friends(currentUser, false).size(),
                                "Current user should have friend after accepting"
                        ),
                        () -> assertEquals(
                                1,
                                nus.friends(incomeInvitation, false).size(),
                                "Target friend should have friend after accepting"
                        )
                )
        );
    }

    @Test
    @DisplayName("REST: Отклонение заявки в друзья")
    @AllureId("200008")
    @Tag("REST")
    @GenerateUser(
            incomeInvitations = @IncomeInvitations(count = 1)
    )
    void declineInvitationTest(@User(selector = METHOD) UserJson user) throws Exception {
        final String currentUser = user.username();
        final String incomeInvitation = user.testData().invitationsJsons().get(0).username();

        FriendJson fj = new FriendJson(incomeInvitation);
        final List<UserJson> invitations = nus.declineInvitation(currentUser, fj);

        step("Check that no invitation present in response", () -> assertTrue(invitations.isEmpty()));

        step("Check that friends request & income invitation removed for both users", () ->
                Assertions.assertAll(
                        () -> assertTrue(
                                nus.invitations(currentUser).isEmpty(),
                                "Current user should not have invitations after declining"),
                        () -> assertTrue(
                                nus.friends(incomeInvitation, true).isEmpty(),
                                "Inviter should not have pending friend after declining"
                        )
                )
        );
    }

    @Test
    @DisplayName("REST: Добавление друга")
    @AllureId("200009")
    @Tag("REST")
    @GenerateUsers({
            @GenerateUser,
            @GenerateUser
    })
    void addFriendTest(@User(selector = METHOD) UserJson[] users) throws Exception {
        final String currentUser = users[0].username();
        final String friendWillBeAdded = users[1].username();

        FriendJson fj = new FriendJson(friendWillBeAdded);
        final UserJson invitation = nus.addFriend(currentUser, fj);

        step("Check invitation in response", () -> {
            assertEquals(friendWillBeAdded, invitation.username());
            assertEquals(FriendState.INVITE_SENT, invitation.friendState());
        });

        step("Check that friends request & income invitation present for both users", () ->
                Assertions.assertAll(
                        () -> assertEquals(
                                1,
                                nus.friends(currentUser, true).size(),
                                "Current user should have pending friend after adding"),
                        () -> assertEquals(
                                1,
                                nus.invitations(friendWillBeAdded).size(),
                                "Target friend should have 1 invitation"
                        )
                )
        );
    }

    @Test
    @DisplayName("REST: Удаление друга")
    @AllureId("200010")
    @Tag("REST")
    @GenerateUser(
            friends = @Friends(count = 1)
    )
    void removeFriendTest(@User(selector = METHOD) UserJson user) throws Exception {
        final String currentUsername = user.username();
        final String friendUsername = user.testData().friendsJsons().get(0).username();
        final List<UserJson> friends = nus.removeFriend(currentUsername, friendUsername);

        step("Check that no friends present in response", () -> assertTrue(friends.isEmpty()));

        step("Check that no friends present in GET /friends request for both users", () ->
                Assertions.assertAll(
                        () -> assertTrue(
                                nus.friends(currentUsername, false).isEmpty(),
                                "Current user should not have friend after removing"
                        ),
                        () -> assertTrue(
                                nus.friends(friendUsername, false).isEmpty(),
                                "Target friend should not have friend after removing"
                        )
                )
        );
    }
}
