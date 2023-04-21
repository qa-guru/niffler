package niffler.test.rest;

import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import niffler.api.NifflerUserdataClient;
import niffler.jupiter.annotation.Friends;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.GenerateUsers;
import niffler.jupiter.annotation.IncomeInvitations;
import niffler.jupiter.annotation.OutcomeInvitations;
import niffler.jupiter.annotation.User;
import niffler.model.rest.FriendJson;
import niffler.model.rest.FriendState;
import niffler.model.rest.UserJson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static io.qameta.allure.Allure.step;
import static niffler.jupiter.extension.CreateUserExtension.Selector.METHOD;
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
        final List<UserJson> friends = nus.friends(user.getUsername(), false);

        step("Check that response contains expected users", () ->
                assertEquals(1, friends.size())
        );

        Optional<UserJson> friend = friends.stream()
                .filter(u -> u.getFriendState() == FriendState.FRIEND)
                .findFirst();

        Optional<UserJson> invitation = friends.stream()
                .filter(u -> u.getFriendState() == FriendState.INVITE_SENT)
                .findFirst();

        step("Check friend in response", () -> {
            assertTrue(friend.isPresent());
            assertEquals(user.getFriendsJsons().get(0).getUsername(), friend.get().getUsername());
            assertEquals(FriendState.FRIEND, friend.get().getFriendState());
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
        final List<UserJson> friends = nus.friends(user.getUsername(), true);

        step("Check that response contains expected users", () ->
                assertEquals(2, friends.size())
        );

        Optional<UserJson> friend = friends.stream()
                .filter(u -> u.getFriendState() == FriendState.FRIEND)
                .findFirst();

        Optional<UserJson> invitation = friends.stream()
                .filter(u -> u.getFriendState() == FriendState.INVITE_SENT)
                .findFirst();

        step("Check friend in response", () -> {
            assertTrue(friend.isPresent());
            assertEquals(user.getFriendsJsons().get(0).getUsername(), friend.get().getUsername());
            assertEquals(FriendState.FRIEND, friend.get().getFriendState());
        });

        step("Check invitation in response", () -> {
            assertTrue(invitation.isPresent());
            assertEquals(user.getInvitationsJsons().get(0).getUsername(), invitation.get().getUsername());
            assertEquals(FriendState.INVITE_SENT, invitation.get().getFriendState());
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
        final List<UserJson> invitations = nus.invitations(user.getUsername());

        step("Check that response contains expected invitations", () ->
                assertEquals(1, invitations.size())
        );

        UserJson invitation = invitations.get(0);

        step("Check invitation in response", () -> {
            assertEquals(user.getInvitationsJsons().get(0).getUsername(), invitation.getUsername());
            assertEquals(FriendState.INVITE_RECEIVED, invitation.getFriendState());
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
        FriendJson fj = new FriendJson();
        fj.setUsername(user.getInvitationsJsons().get(0).getUsername());

        final List<UserJson> friends = nus.acceptInvitation(user.getUsername(), fj);
        UserJson friend = friends.get(0);

        step("Check friend in response", () -> {
            assertEquals(user.getInvitationsJsons().get(0).getUsername(), friend.getUsername());
            assertEquals(FriendState.FRIEND, friend.getFriendState());
        });
    }

    @Test
    @DisplayName("REST: Отклонение заявки в друзья")
    @AllureId("200008")
    @Tag("REST")
    @GenerateUser(
            incomeInvitations = @IncomeInvitations(count = 1)
    )
    void declineInvitationTest(@User(selector = METHOD) UserJson user) throws Exception {
        FriendJson fj = new FriendJson();
        fj.setUsername(user.getInvitationsJsons().get(0).getUsername());

        final List<UserJson> invitations = nus.declineInvitation(user.getUsername(), fj);

        step("Check that no invitation present in response", () -> assertTrue(invitations.isEmpty()));
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
        final UserJson currentUser = users[0];
        final UserJson friend = users[1];

        FriendJson fj = new FriendJson();
        fj.setUsername(friend.getUsername());

        final UserJson invitation = nus.addFriend(currentUser.getUsername(), fj);

        step("Check invitation in response", () -> {
            assertEquals(friend.getUsername(), invitation.getUsername());
            assertEquals(FriendState.INVITE_SENT, invitation.getFriendState());
        });
    }

    @Test
    @DisplayName("REST: Удаление друга")
    @AllureId("200010")
    @Tag("REST")
    @GenerateUser(
            friends = @Friends(count = 1)
    )
    void removeFriendTest(@User(selector = METHOD) UserJson user) throws Exception {
        final List<UserJson> friends = nus.removeFriend(user.getUsername(), user.getFriendsJsons().get(0).getUsername());

        step("Check that no friends present in response", () -> {
            assertTrue(friends.isEmpty());
        });
    }
}
