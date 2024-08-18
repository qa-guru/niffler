package guru.qa.niffler.test.soap;

import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.GenerateUsers;
import guru.qa.niffler.jupiter.annotation.IncomeInvitations;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import jaxb.userdata.AcceptInvitationRequest;
import jaxb.userdata.AllUsersRequest;
import jaxb.userdata.DeclineInvitationRequest;
import jaxb.userdata.FriendState;
import jaxb.userdata.FriendsRequest;
import jaxb.userdata.SendInvitationRequest;
import jaxb.userdata.UserResponse;
import jaxb.userdata.UsersResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
  @DisplayName("SOAP: Прием заявки в друзья")
  @AllureId("100011")
  @Tag("SOAP")
  @GenerateUser(
      incomeInvitations = @IncomeInvitations(count = 1)
  )
  void acceptInvitationTest(@User(selector = METHOD) UserJson user) throws Exception {
    final String currentUsername = user.username();
    final UserJson incomeInvitationUser = user.testData().incomeInvitations().getFirst();

    AcceptInvitationRequest air = acceptInvitationRequest(currentUsername, incomeInvitationUser.username());

    final UserResponse friend = wsClient.acceptInvitationRequest(air);
    step("Check that response not null", () ->
        assertNotNull(friend)
    );

    step("Check friend in response", () -> {
      assertEquals(incomeInvitationUser.username(), friend.getUser().getUsername());
      assertSame(FriendState.FRIEND, friend.getUser().getFriendState());
    });

    step("Check that friends present in /friends request for both users", () -> {
          UsersResponse currentUserResponse = wsClient.friendsRequest(friendsRequest(currentUsername, null));
          UsersResponse targetUserResponse = wsClient.friendsRequest(friendsRequest(incomeInvitationUser.username(), null));
          Assertions.assertAll(
              () -> assertEquals(
                  1,
                  currentUserResponse.getUser().size(),
                  "Current user should have friend after accepting"
              ),
              () -> assertEquals(
                  FriendState.FRIEND,
                  currentUserResponse.getUser().getFirst().getFriendState(),
                  "Current user should have friend after accepting"
              ),
              () -> assertEquals(
                  1,
                  targetUserResponse.getUser().size(),
                  "Target user should have friend after accepting"
              ),
              () -> assertEquals(
                  FriendState.FRIEND,
                  targetUserResponse.getUser().getFirst().getFriendState(),
                  "Target user should have friend after accepting"
              )
          );
        }
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
    final UserJson incomeInvitationUser = user.testData().incomeInvitations().getFirst();

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
                wsClient.friendsRequest(friendsRequest(currentUsername, null))
                    .getUser()
                    .isEmpty(),
                "Current user should not have income invitations after declining"),
            () -> assertEquals(
                FriendState.VOID,
                wsClient.allUsersRequest(allUsersRequest(incomeInvitationUser.username(), null))
                    .getUser()
                    .getFirst()
                    .getFriendState(),
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
                FriendState.INVITE_SENT,
                wsClient.allUsersRequest(allUsersRequest(currentUser, null))
                    .getUser()
                    .getFirst()
                    .getFriendState(),
                "Current user should have outcome invitation after adding"),
            () -> assertEquals(
                1,
                wsClient.friendsRequest(friendsRequest(friendWillBeAdded, null))
                    .getUser()
                    .size(),
                "Target friend should have 1 income invitation"
            )
        )
    );
  }

  private @Nonnull AllUsersRequest allUsersRequest(@Nonnull String username, @Nullable String searchQuery) {
    AllUsersRequest au = new AllUsersRequest();
    au.setUsername(username);
    au.setSearchQuery(searchQuery);
    return au;
  }

  private @Nonnull FriendsRequest friendsRequest(@Nonnull String username, @Nullable String searchQuery) {
    FriendsRequest fr = new FriendsRequest();
    fr.setUsername(username);
    fr.setSearchQuery(searchQuery);
    return fr;
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
