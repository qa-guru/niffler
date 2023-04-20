package niffler.test.soap;

import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import niffler.jupiter.annotation.Friends;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.GenerateUsers;
import niffler.jupiter.annotation.IncomeInvitations;
import niffler.jupiter.annotation.OutcomeInvitations;
import niffler.jupiter.annotation.User;
import niffler.model.rest.UserJson;
import niffler.userdata.wsdl.AcceptInvitationRequest;
import niffler.userdata.wsdl.AcceptInvitationResponse;
import niffler.userdata.wsdl.AddFriendRequest;
import niffler.userdata.wsdl.AddFriendResponse;
import niffler.userdata.wsdl.DeclineInvitationRequest;
import niffler.userdata.wsdl.DeclineInvitationResponse;
import niffler.userdata.wsdl.Friend;
import niffler.userdata.wsdl.FriendState;
import niffler.userdata.wsdl.FriendsRequest;
import niffler.userdata.wsdl.FriendsResponse;
import niffler.userdata.wsdl.InvitationsRequest;
import niffler.userdata.wsdl.InvitationsResponse;
import niffler.userdata.wsdl.RemoveFriendRequest;
import niffler.userdata.wsdl.RemoveFriendResponse;
import niffler.ws.NifflerUserdataWsService;
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

@Epic("[SOAP][niffler-userdata]: Друзья")
@DisplayName("[SOAP][niffler-userdata]: Друзья")
public class UserDataFriendsSoapTest extends BaseSoapTest {

    private final NifflerUserdataWsService nus = new NifflerUserdataWsService();

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
        FriendsRequest fr = new FriendsRequest();
        fr.setUsername(user.getUsername());
        fr.setIncludePending(false);

        final FriendsResponse friendsResponse = nus.friendsRequest(fr);

        List<niffler.userdata.wsdl.User> friends = friendsResponse.getUser();

        step("Check that response contains expected users", () ->
                assertEquals(1, friends.size())
        );

        Optional<niffler.userdata.wsdl.User> friend = friends.stream()
                .filter(u -> u.getFriendState() == FriendState.FRIEND)
                .findFirst();

        Optional<niffler.userdata.wsdl.User> invitation = friends.stream()
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
    @DisplayName("SOAP: Для пользователя должен возвращаться список друзей и инвайтов из niffler-userdata" +
            " при передаче includePending = true")
    @AllureId("100005")
    @Tag("SOAP")
    @GenerateUser(
            friends = @Friends(count = 1),
            outcomeInvitations = @OutcomeInvitations(count = 1)
    )
    void getAllFriendsListWithInvitationTest(@User(selector = METHOD) UserJson user) throws Exception {
        FriendsRequest fr = new FriendsRequest();
        fr.setUsername(user.getUsername());
        fr.setIncludePending(true);

        final FriendsResponse friendsResponse = nus.friendsRequest(fr);

        List<niffler.userdata.wsdl.User> friends = friendsResponse.getUser();

        step("Check that response contains expected users", () ->
                assertEquals(2, friends.size())
        );

        Optional<niffler.userdata.wsdl.User> friend = friends.stream()
                .filter(u -> u.getFriendState() == FriendState.FRIEND)
                .findFirst();

        Optional<niffler.userdata.wsdl.User> invitation = friends.stream()
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
    @DisplayName("SOAP: Для пользователя должен возвращаться список приглашений дружить из niffler-userdata")
    @AllureId("100006")
    @Tag("SOAP")
    @GenerateUser(
            incomeInvitations = @IncomeInvitations(count = 1)
    )
    void getInvitationTest(@User(selector = METHOD) UserJson user) throws Exception {
        InvitationsRequest ir = new InvitationsRequest();
        ir.setUsername(user.getUsername());

        final InvitationsResponse invitationsResponse = nus.invitationsRequest(ir);

        List<niffler.userdata.wsdl.User> invitations = invitationsResponse.getUser();

        step("Check that response contains expected invitations", () ->
                assertEquals(1, invitations.size())
        );

        niffler.userdata.wsdl.User invitation = invitationsResponse.getUser().get(0);

        step("Check invitation in response", () -> {
            assertEquals(user.getInvitationsJsons().get(0).getUsername(), invitation.getUsername());
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
        AcceptInvitationRequest air = new AcceptInvitationRequest();
        air.setUsername(user.getUsername());
        Friend fr = new Friend();
        fr.setUsername(user.getInvitationsJsons().get(0).getUsername());
        air.setInvitation(fr);

        final AcceptInvitationResponse acceptInvitationResponse = nus.acceptInvitationRequest(air);

        niffler.userdata.wsdl.User friend = acceptInvitationResponse.getUser().get(0);

        step("Check friend in response", () -> {
            assertEquals(user.getInvitationsJsons().get(0).getUsername(), friend.getUsername());
            assertEquals(FriendState.FRIEND, friend.getFriendState());
        });
    }

    @Test
    @DisplayName("SOAP: Отклонение заявки в друзья")
    @AllureId("100008")
    @Tag("SOAP")
    @GenerateUser(
            incomeInvitations = @IncomeInvitations(count = 1)
    )
    void declineInvitationTest(@User(selector = METHOD) UserJson user) throws Exception {
        DeclineInvitationRequest dir = new DeclineInvitationRequest();
        dir.setUsername(user.getUsername());
        Friend fr = new Friend();
        fr.setUsername(user.getInvitationsJsons().get(0).getUsername());
        dir.setInvitation(fr);

        final DeclineInvitationResponse declineInvitationResponse = nus.declineInvitationRequest(dir);

        List<niffler.userdata.wsdl.User> invitations = declineInvitationResponse.getUser();

        step("Check that no invitation present in response", () -> assertTrue(invitations.isEmpty()));
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
        final UserJson currentUser = users[0];
        final UserJson friend = users[1];

        AddFriendRequest afr = new AddFriendRequest();
        afr.setUsername(currentUser.getUsername());
        Friend fr = new Friend();
        fr.setUsername(friend.getUsername());
        afr.setFriend(fr);

        final AddFriendResponse addFriendResponse = nus.addFriendRequest(afr);

        niffler.userdata.wsdl.User invitation = addFriendResponse.getUser();

        step("Check invitation in response", () -> {
            assertEquals(friend.getUsername(), invitation.getUsername());
            assertEquals(FriendState.INVITE_SENT, invitation.getFriendState());
        });
    }

    @Test
    @DisplayName("SOAP: Удаление друга")
    @AllureId("100010")
    @Tag("SOAP")
    @GenerateUser(
            friends = @Friends(count = 1)
    )
    void removeFriendTest(@User(selector = METHOD) UserJson user) throws Exception {
        RemoveFriendRequest rfr = new RemoveFriendRequest();
        rfr.setUsername(user.getUsername());
        rfr.setFriendUsername(user.getFriendsJsons().get(0).getUsername());

        final RemoveFriendResponse removeFriendResponse = nus.removeFriendRequest(rfr);

        List<niffler.userdata.wsdl.User> friends = removeFriendResponse.getUser();

        step("Check that no friends present in response", () -> {
            assertTrue(friends.isEmpty());
        });
    }
}
