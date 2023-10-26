package guru.qa.niffler.test.soap;

import guru.qa.niffler.jupiter.annotation.Friends;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.GenerateUsers;
import guru.qa.niffler.jupiter.annotation.IncomeInvitations;
import guru.qa.niffler.jupiter.annotation.OutcomeInvitations;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.userdata.wsdl.AcceptInvitationRequest;
import guru.qa.niffler.userdata.wsdl.AcceptInvitationResponse;
import guru.qa.niffler.userdata.wsdl.AddFriendRequest;
import guru.qa.niffler.userdata.wsdl.AddFriendResponse;
import guru.qa.niffler.userdata.wsdl.DeclineInvitationRequest;
import guru.qa.niffler.userdata.wsdl.DeclineInvitationResponse;
import guru.qa.niffler.userdata.wsdl.Friend;
import guru.qa.niffler.userdata.wsdl.FriendState;
import guru.qa.niffler.userdata.wsdl.FriendsRequest;
import guru.qa.niffler.userdata.wsdl.FriendsResponse;
import guru.qa.niffler.userdata.wsdl.InvitationsRequest;
import guru.qa.niffler.userdata.wsdl.InvitationsResponse;
import guru.qa.niffler.userdata.wsdl.RemoveFriendRequest;
import guru.qa.niffler.userdata.wsdl.RemoveFriendResponse;
import guru.qa.niffler.ws.UserdataWsClient;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.jupiter.annotation.User.Selector.METHOD;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("[SOAP][niffler-userdata]: Друзья")
@DisplayName("[SOAP][niffler-userdata]: Друзья")
public class UserDataFriendsSoapTest extends BaseSoapTest {

    private static final UserdataWsClient wsClient = new UserdataWsClient();

    @Test
    @DisplayName("SOAP: Для пользователя должен возвращаться список друзей из niffler-userdata" +
            " при передаче includePending = false")
    @AllureId("100004")
    @Tag("SOAP")
    @GenerateUser(
            friends = @Friends(count = 1),
            outcomeInvitations = @OutcomeInvitations(count = 1)
    )
    void getAllFriendsListWithoutInvitationTest(@User(selector = METHOD) UserJson user) throws Exception {
        UserJson testFriend = user.testData().friendsJsons().get(0);
        FriendsRequest fr = friendsRequest(user.username(), false);

        final FriendsResponse friendsResponse = wsClient.friendsRequest(fr);

        List<guru.qa.niffler.userdata.wsdl.User> friends = friendsResponse.getUser();

        step("Check that response contains expected users", () ->
                assertEquals(1, friends.size())
        );

        Optional<guru.qa.niffler.userdata.wsdl.User> friend = friends.stream()
                .filter(u -> u.getFriendState() == FriendState.FRIEND)
                .findFirst();

        Optional<guru.qa.niffler.userdata.wsdl.User> invitation = friends.stream()
                .filter(u -> u.getFriendState() == FriendState.INVITE_SENT)
                .findFirst();

        step("Check friend in response", () -> {
            assertTrue(friend.isPresent());
            assertEquals(testFriend.id(), UUID.fromString(friend.get().getId()));
            assertEquals(testFriend.username(), friend.get().getUsername());
            assertEquals(FriendState.FRIEND, friend.get().getFriendState());
        });

        step(
                "Check that no outcome invitation present in response",
                () -> assertFalse(invitation.isPresent())
        );
    }

    @Test
    @DisplayName("SOAP: Для пользователя должен возвращаться список друзей и инвайтов из niffler-userdata" +
            " при передаче includePending = true")
    @AllureId("100005")
    @Tag("SOAP")
    @GenerateUser(
            friends = @Friends(count = 1),
            outcomeInvitations = @OutcomeInvitations(count = 1)
    )
    void getAllFriendsListWithInvitationTest(@User(selector = METHOD) UserJson user) throws Exception {
        UserJson testFriend = user.testData().friendsJsons().get(0);
        UserJson testOutInvitation = user.testData().invitationsJsons().get(0);
        FriendsRequest fr = friendsRequest(user.username(), true);

        final FriendsResponse friendsResponse = wsClient.friendsRequest(fr);

        List<guru.qa.niffler.userdata.wsdl.User> friends = friendsResponse.getUser();

        step("Check that response contains expected users", () ->
                assertEquals(2, friends.size())
        );

        Optional<guru.qa.niffler.userdata.wsdl.User> friend = friends.stream()
                .filter(u -> u.getFriendState() == FriendState.FRIEND)
                .findFirst();

        Optional<guru.qa.niffler.userdata.wsdl.User> invitation = friends.stream()
                .filter(u -> u.getFriendState() == FriendState.INVITE_SENT)
                .findFirst();

        step("Check friend in response", () -> {
            assertTrue(friend.isPresent());
            assertEquals(testFriend.id(), UUID.fromString(friend.get().getId()));
            assertEquals(testFriend.username(), friend.get().getUsername());
            assertEquals(FriendState.FRIEND, friend.get().getFriendState());
        });

        step("Check invitation in response", () -> {
            assertTrue(invitation.isPresent());
            assertEquals(testOutInvitation.id(), UUID.fromString(invitation.get().getId()));
            assertEquals(testOutInvitation.username(), invitation.get().getUsername());
            assertEquals(FriendState.INVITE_SENT, invitation.get().getFriendState());
        });
    }

    @Test
    @DisplayName("SOAP: Для пользователя должен возвращаться список приглашений дружить из niffler-userdata")
    @AllureId("100006")
    @Tag("SOAP")
    @GenerateUser(
            incomeInvitations = @IncomeInvitations(count = 1)
    )
    void getInvitationTest(@User(selector = METHOD) UserJson user) throws Exception {
        InvitationsRequest ir = new InvitationsRequest();
        ir.setUsername(user.username());

        final InvitationsResponse invitationsResponse = wsClient.invitationsRequest(ir);

        List<guru.qa.niffler.userdata.wsdl.User> invitations = invitationsResponse.getUser();

        step("Check that response contains expected invitations", () ->
                assertEquals(1, invitations.size())
        );

        guru.qa.niffler.userdata.wsdl.User invitation = invitationsResponse.getUser().get(0);

        step("Check invitation in response", () -> {
            assertEquals(user.testData().invitationsJsons().get(0).username(), invitation.getUsername());
            assertEquals(FriendState.INVITE_RECEIVED, invitation.getFriendState());
        });
    }

    @Test
    @DisplayName("SOAP: Прием заявки в друзья")
    @AllureId("100007")
    @Tag("SOAP")
    @GenerateUser(
            incomeInvitations = @IncomeInvitations(count = 1)
    )
    void acceptInvitationTest(@User(selector = METHOD) UserJson user) throws Exception {
        final String currentUser = user.username();
        final String incomeInvitation = user.testData().invitationsJsons().get(0).username();

        AcceptInvitationRequest air = acceptInvitationRequest(currentUser, incomeInvitation);

        final AcceptInvitationResponse acceptInvitationResponse = wsClient.acceptInvitationRequest(air);

        guru.qa.niffler.userdata.wsdl.User friend = acceptInvitationResponse.getUser().get(0);

        step("Check friend in response", () -> {
            assertEquals(user.testData().invitationsJsons().get(0).username(), friend.getUsername());
            assertEquals(FriendState.FRIEND, friend.getFriendState());
        });

        step("Check that friends present in GET /friends request for both users", () ->
                Assertions.assertAll(
                        () -> assertEquals(
                                1,
                                wsClient.friendsRequest(friendsRequest(currentUser, false))
                                        .getUser()
                                        .size(),
                                "Current user should have friend after accepting"
                        ),
                        () -> assertEquals(
                                1,
                                wsClient.friendsRequest(friendsRequest(incomeInvitation, false))
                                        .getUser()
                                        .size(),
                                "Target friend should have friend after accepting"
                        )
                )
        );
    }

    @Test
    @DisplayName("SOAP: Отклонение заявки в друзья")
    @AllureId("100008")
    @Tag("SOAP")
    @GenerateUser(
            incomeInvitations = @IncomeInvitations(count = 1)
    )
    void declineInvitationTest(@User(selector = METHOD) UserJson user) throws Exception {
        final String currentUser = user.username();
        final String incomeInvitation = user.testData().invitationsJsons().get(0).username();

        DeclineInvitationRequest dir = declineInvitationRequest(currentUser, incomeInvitation);

        final DeclineInvitationResponse declineInvitationResponse = wsClient.declineInvitationRequest(dir);

        List<guru.qa.niffler.userdata.wsdl.User> invitations = declineInvitationResponse.getUser();

        step("Check that no invitation present in response", () -> assertTrue(invitations.isEmpty()));

        step("Check that friends request & income invitation removed for both users", () ->
                Assertions.assertAll(
                        () -> assertTrue(
                                wsClient.invitationsRequest(invitationsRequest(currentUser))
                                        .getUser()
                                        .isEmpty(),
                                "Current user should not have invitations after declining"),
                        () -> assertTrue(
                                wsClient.friendsRequest(friendsRequest(incomeInvitation, true))
                                        .getUser()
                                        .isEmpty(),
                                "Inviter should not have pending friend after declining"
                        )
                )
        );
    }

    @Test
    @DisplayName("SOAP: Добавление друга")
    @AllureId("100009")
    @Tag("SOAP")
    @GenerateUsers({
            @GenerateUser,
            @GenerateUser
    })
    void addFriendTest(@User(selector = METHOD) UserJson[] users) throws Exception {
        final String currentUser = users[0].username();
        final String friendWillBeAdded = users[1].username();

        AddFriendRequest afr = addFriendRequest(currentUser, friendWillBeAdded);

        final AddFriendResponse addFriendResponse = wsClient.addFriendRequest(afr);

        guru.qa.niffler.userdata.wsdl.User invitation = addFriendResponse.getUser();

        step("Check invitation in response", () -> {
            assertEquals(friendWillBeAdded, invitation.getUsername());
            assertEquals(FriendState.INVITE_SENT, invitation.getFriendState());
        });

        step("Check that friends request & income invitation present for both users", () ->
                Assertions.assertAll(
                        () -> assertEquals(
                                1,
                                wsClient.friendsRequest(friendsRequest(currentUser, true))
                                        .getUser()
                                        .size(),
                                "Current user should have pending friend after adding"),
                        () -> assertEquals(
                                1,
                                wsClient.invitationsRequest(invitationsRequest(friendWillBeAdded))
                                        .getUser()
                                        .size(),
                                "Target friend should have 1 invitation"
                        )
                )
        );
    }

    @Test
    @DisplayName("SOAP: Удаление друга")
    @AllureId("100010")
    @Tag("SOAP")
    @GenerateUser(
            friends = @Friends(count = 1)
    )
    void removeFriendTest(@User(selector = METHOD) UserJson user) throws Exception {
        final String currentUsername = user.username();
        final String friendUsername = user.testData().friendsJsons().get(0).username();

        RemoveFriendRequest rfr = new RemoveFriendRequest();
        rfr.setUsername(user.username());
        rfr.setFriendUsername(user.testData().friendsJsons().get(0).username());

        final RemoveFriendResponse removeFriendResponse = wsClient.removeFriendRequest(rfr);

        List<guru.qa.niffler.userdata.wsdl.User> friends = removeFriendResponse.getUser();

        step("Check that no friends present in response", () -> assertTrue(friends.isEmpty()));

        step("Check that no friends present in GET /friends request for both users", () ->
                Assertions.assertAll(
                        () -> assertTrue(
                                wsClient.friendsRequest(friendsRequest(currentUsername, false))
                                        .getUser()
                                        .isEmpty(),
                                "Current user should not have friend after removing"
                        ),
                        () -> assertTrue(
                                wsClient.friendsRequest(friendsRequest(friendUsername, false))
                                        .getUser()
                                        .isEmpty(),
                                "Target friend should not have friend after removing"
                        )
                )
        );
    }

    private FriendsRequest friendsRequest(String username, boolean includePending) {
        FriendsRequest fr = new FriendsRequest();
        fr.setUsername(username);
        fr.setIncludePending(includePending);
        return fr;
    }

    private AddFriendRequest addFriendRequest(String username, String friendUsername) {
        AddFriendRequest afr = new AddFriendRequest();
        afr.setUsername(username);
        Friend fr = new Friend();
        fr.setUsername(friendUsername);
        afr.setFriend(fr);
        return afr;
    }

    private InvitationsRequest invitationsRequest(String username) {
        InvitationsRequest ir = new InvitationsRequest();
        ir.setUsername(username);
        return ir;
    }

    private DeclineInvitationRequest declineInvitationRequest(String username, String inviterUsername) {
        DeclineInvitationRequest dir = new DeclineInvitationRequest();
        dir.setUsername(username);
        Friend fr = new Friend();
        fr.setUsername(inviterUsername);
        dir.setInvitation(fr);
        return dir;
    }

    private AcceptInvitationRequest acceptInvitationRequest(String username, String inviterUsername) {
        AcceptInvitationRequest air = new AcceptInvitationRequest();
        air.setUsername(username);
        Friend fr = new Friend();
        fr.setUsername(inviterUsername);
        air.setInvitation(fr);
        return air;
    }
}
