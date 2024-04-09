package guru.qa.niffler.test.soap;

import guru.qa.niffler.jupiter.annotation.Friends;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.GenerateUsers;
import guru.qa.niffler.jupiter.annotation.IncomeInvitations;
import guru.qa.niffler.jupiter.annotation.OutcomeInvitations;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.userdata.wsdl.AcceptInvitationRequest;
import guru.qa.niffler.userdata.wsdl.DeclineInvitationRequest;
import guru.qa.niffler.userdata.wsdl.FriendState;
import guru.qa.niffler.userdata.wsdl.FriendsRequest;
import guru.qa.niffler.userdata.wsdl.IncomeInvitationsRequest;
import guru.qa.niffler.userdata.wsdl.OutcomeInvitationsRequest;
import guru.qa.niffler.userdata.wsdl.SendInvitationRequest;
import guru.qa.niffler.userdata.wsdl.UserResponse;
import guru.qa.niffler.userdata.wsdl.UsersResponse;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

import static guru.qa.niffler.jupiter.annotation.User.Selector.METHOD;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("[SOAP][niffler-userdata]: Приглашения")
@DisplayName("[SOAP][niffler-userdata]: Приглашения")
public class UserDataInvitationsSoapTest extends BaseSoapTest {

    @Test
    @DisplayName("SOAP: Для пользователя должен возвращаться список исходящих инвайтов из niffler-userdata")
    @AllureId("100007")
    @Tag("SOAP")
    @GenerateUser(
            friends = @Friends(count = 1),
            outcomeInvitations = @OutcomeInvitations(count = 1)
    )
    void getAllOutcomeInvitationsListTest(@User(selector = METHOD) UserJson user) throws Exception {
        UserJson testOutInvitation = user.testData().outcomeInvitations().get(0);
        OutcomeInvitationsRequest oir = outcomeInvitationsRequest(user.username(), null);

        final UsersResponse usersResponse = wsClient.outcomeInvitationsRequest(oir);
        step("Check that response not null", () ->
                assertNotNull(usersResponse)
        );
        step("Check response list size", () ->
                assertEquals(1, usersResponse.getUser().size())
        );

        final var foundedInvitation = usersResponse.getUser().get(0);

        step("Check outcome invitation in response", () -> {
            assertSame(FriendState.INVITE_SENT, foundedInvitation.getFriendState());
            assertEquals(testOutInvitation.id(), UUID.fromString(foundedInvitation.getId()));
            assertEquals(testOutInvitation.username(), foundedInvitation.getUsername());
        });
    }

    @Test
    @DisplayName("SOAP: Для пользователя должен возвращаться список исходящих инвайтов из niffler-userdata " +
            "с фильтраций по username, если передан searchQuery")
    @AllureId("100008")
    @Tag("SOAP")
    @GenerateUser(
            friends = @Friends(count = 1),
            outcomeInvitations = @OutcomeInvitations(count = 2)
    )
    void getFilteredOutcomeInvitationsListTest(@User(selector = METHOD) UserJson user) throws Exception {
        UserJson testOutInvitation = user.testData().outcomeInvitations().get(0);
        OutcomeInvitationsRequest oir = outcomeInvitationsRequest(user.username(), testOutInvitation.username());

        final UsersResponse usersResponse = wsClient.outcomeInvitationsRequest(oir);
        step("Check that response not null", () ->
                assertNotNull(usersResponse)
        );
        step("Check response list size", () ->
                assertEquals(1, usersResponse.getUser().size())
        );

        final var foundedInvitation = usersResponse.getUser().get(0);

        step("Check outcome invitation in response", () -> {
            assertSame(FriendState.INVITE_SENT, foundedInvitation.getFriendState());
            assertEquals(testOutInvitation.id(), UUID.fromString(foundedInvitation.getId()));
            assertEquals(testOutInvitation.username(), foundedInvitation.getUsername());
        });
    }

    @Test
    @DisplayName("SOAP: Для пользователя должен возвращаться список приглашений дружить из niffler-userdata")
    @AllureId("100009")
    @Tag("SOAP")
    @GenerateUser(
            friends = @Friends(count = 1),
            incomeInvitations = @IncomeInvitations(count = 1)
    )
    void getAllIncomeInvitationsListTest(@User(selector = METHOD) UserJson user) throws Exception {
        UserJson testInvitation = user.testData().incomeInvitations().get(0);
        IncomeInvitationsRequest ir = incomeInvitationsRequest(user.username(), null);

        final UsersResponse usersResponse = wsClient.incomeInvitationsRequest(ir);
        step("Check that response not null", () ->
                assertNotNull(usersResponse)
        );
        step("Check response list size", () ->
                assertEquals(1, usersResponse.getUser().size())
        );

        final var foundedInvitation = usersResponse.getUser().get(0);

        step("Check income invitation in response", () -> {
            assertSame(FriendState.INVITE_RECEIVED, foundedInvitation.getFriendState());
            assertEquals(testInvitation.id(), UUID.fromString(foundedInvitation.getId()));
            assertEquals(testInvitation.username(), foundedInvitation.getUsername());
        });
    }

    @Test
    @DisplayName("SOAP: Для пользователя должен возвращаться список приглашений дружить из niffler-userdata " +
            "с фильтраций по username, если передан searchQuery")
    @AllureId("100010")
    @Tag("SOAP")
    @GenerateUser(
            friends = @Friends(count = 1),
            incomeInvitations = @IncomeInvitations(count = 2)
    )
    void getFilteredIncomeInvitationsListTest(@User(selector = METHOD) UserJson user) throws Exception {
        UserJson testInvitation = user.testData().incomeInvitations().get(0);
        IncomeInvitationsRequest ir = incomeInvitationsRequest(user.username(), testInvitation.username());

        final UsersResponse usersResponse = wsClient.incomeInvitationsRequest(ir);
        step("Check that response not null", () ->
                assertNotNull(usersResponse)
        );
        step("Check response list size", () ->
                assertEquals(1, usersResponse.getUser().size())
        );

        final var foundedInvitation = usersResponse.getUser().get(0);

        step("Check income invitation in response", () -> {
            assertSame(FriendState.INVITE_RECEIVED, foundedInvitation.getFriendState());
            assertEquals(testInvitation.id(), UUID.fromString(foundedInvitation.getId()));
            assertEquals(testInvitation.username(), foundedInvitation.getUsername());
        });
    }

    @Test
    @DisplayName("SOAP: Прием заявки в друзья")
    @AllureId("100011")
    @Tag("SOAP")
    @GenerateUser(
            incomeInvitations = @IncomeInvitations(count = 1)
    )
    void acceptInvitationTest(@User(selector = METHOD) UserJson user) throws Exception {
        final String currentUsername = user.username();
        final UserJson incomeInvitationUser = user.testData().incomeInvitations().get(0);

        AcceptInvitationRequest air = acceptInvitationRequest(currentUsername, incomeInvitationUser.username());

        final UserResponse friend = wsClient.acceptInvitationRequest(air);
        step("Check that response not null", () ->
                assertNotNull(friend)
        );

        step("Check friend in response", () -> {
            assertEquals(incomeInvitationUser.username(), friend.getUser().getUsername());
            assertSame(FriendState.FRIEND, friend.getUser().getFriendState());
        });

        step("Check that friends present in /friends request for both users", () ->
                Assertions.assertAll(
                        () -> assertEquals(
                                1,
                                wsClient.friendsRequest(friendsRequest(currentUsername, null))
                                        .getUser()
                                        .size(),
                                "Current user should have friend after accepting"
                        ),
                        () -> assertEquals(
                                1,
                                wsClient.friendsRequest(friendsRequest(incomeInvitationUser.username(), null))
                                        .getUser()
                                        .size(),
                                "Target friend should have friend after accepting"
                        )
                )
        );
    }

    @Test
    @DisplayName("SOAP: Отклонение заявки в друзья")
    @AllureId("100012")
    @Tag("SOAP")
    @GenerateUser(
            incomeInvitations = @IncomeInvitations(count = 1)
    )
    void declineInvitationTest(@User(selector = METHOD) UserJson user) throws Exception {
        final String currentUsername = user.username();
        final UserJson incomeInvitationUser = user.testData().incomeInvitations().get(0);

        DeclineInvitationRequest dir = declineInvitationRequest(currentUsername, incomeInvitationUser.username());

        final UserResponse declinedFriend = wsClient.declineInvitationRequest(dir);
        step("Check that response not null", () ->
                assertNotNull(declinedFriend)
        );

        step("Check declined friend in response", () -> {
            assertEquals(incomeInvitationUser.username(), declinedFriend.getUser().getUsername());
            assertSame(FriendState.VOID, declinedFriend.getUser().getFriendState());
        });

        step("Check that friends request & income invitation removed for both users", () ->
                Assertions.assertAll(
                        () -> assertTrue(
                                wsClient.incomeInvitationsRequest(incomeInvitationsRequest(currentUsername, null))
                                        .getUser()
                                        .isEmpty(),
                                "Current user should not have income invitations after declining"),
                        () -> assertTrue(
                                wsClient.outcomeInvitationsRequest(outcomeInvitationsRequest(incomeInvitationUser.username(), null))
                                        .getUser()
                                        .isEmpty(),
                                "Inviter should not have outcome invitations after declining"
                        ),
                        () -> assertTrue(
                                wsClient.friendsRequest(friendsRequest(incomeInvitationUser.username(), null))
                                        .getUser()
                                        .isEmpty(),
                                "Inviter should not have friends after declining"
                        )
                )
        );
    }

    @Test
    @DisplayName("SOAP: Отправка приглашения дружить")
    @AllureId("100013")
    @Tag("SOAP")
    @GenerateUsers({
            @GenerateUser,
            @GenerateUser
    })
    void sendInvitationTest(@User(selector = METHOD) UserJson[] users) throws Exception {
        final String currentUser = users[0].username();
        final String friendWillBeAdded = users[1].username();

        SendInvitationRequest sir = sendInvitationsRequest(currentUser, friendWillBeAdded);

        final UserResponse targetUserResponse = wsClient.sendInvitationRequest(sir);
        step("Check that response not null", () ->
                assertNotNull(targetUserResponse)
        );

        step("Check invitation in response", () -> {
            assertEquals(friendWillBeAdded, targetUserResponse.getUser().getUsername());
            assertEquals(FriendState.INVITE_SENT, targetUserResponse.getUser().getFriendState());
        });

        step("Check that friends request & income invitation present for both users", () ->
                Assertions.assertAll(
                        () -> assertEquals(
                                1,
                                wsClient.outcomeInvitationsRequest(outcomeInvitationsRequest(currentUser, null))
                                        .getUser()
                                        .size(),
                                "Current user should have outcome invitation after adding"),
                        () -> assertEquals(
                                1,
                                wsClient.incomeInvitationsRequest(incomeInvitationsRequest(friendWillBeAdded, null))
                                        .getUser()
                                        .size(),
                                "Target friend should have 1 income invitation"
                        )
                )
        );
    }

    private @Nonnull FriendsRequest friendsRequest(@Nonnull String username, @Nullable String searchQuery) {
        FriendsRequest fr = new FriendsRequest();
        fr.setUsername(username);
        fr.setSearchQuery(searchQuery);
        return fr;
    }

    private @Nonnull IncomeInvitationsRequest incomeInvitationsRequest(@Nonnull String username, @Nullable String searchQuery) {
        IncomeInvitationsRequest ir = new IncomeInvitationsRequest();
        ir.setUsername(username);
        ir.setSearchQuery(searchQuery);
        return ir;
    }

    private @Nonnull OutcomeInvitationsRequest outcomeInvitationsRequest(@Nonnull String username, @Nullable String searchQuery) {
        OutcomeInvitationsRequest or = new OutcomeInvitationsRequest();
        or.setUsername(username);
        or.setSearchQuery(searchQuery);
        return or;
    }

    private @Nonnull SendInvitationRequest sendInvitationsRequest(@Nonnull String username, @Nonnull String friendUsername) {
        SendInvitationRequest sir = new SendInvitationRequest();
        sir.setUsername(username);
        sir.setFriendToBeRequested(friendUsername);
        return sir;
    }


    private @Nonnull DeclineInvitationRequest declineInvitationRequest(@Nonnull String username, @Nonnull String inviterUsername) {
        DeclineInvitationRequest dir = new DeclineInvitationRequest();
        dir.setUsername(username);
        dir.setInvitationToBeDeclined(inviterUsername);
        return dir;
    }

    private @Nonnull AcceptInvitationRequest acceptInvitationRequest(@Nonnull String username, @Nonnull String inviterUsername) {
        AcceptInvitationRequest air = new AcceptInvitationRequest();
        air.setUsername(username);
        air.setFriendToBeAdded(inviterUsername);
        return air;
    }
}
