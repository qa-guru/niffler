package guru.qa.niffler.test.rest;

import guru.qa.niffler.api.UserdataApiClient;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.GenerateUsers;
import guru.qa.niffler.jupiter.annotation.IncomeInvitations;
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

import static guru.qa.niffler.jupiter.annotation.User.Selector.METHOD;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("[REST][niffler-gateway]: Приглашения")
@DisplayName("[REST][niffler-gateway]: Приглашения")
public class GatewayInvitationsRestTest extends BaseRestTest {

  private static final UserdataApiClient userdataApiClient = new UserdataApiClient();

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
    final String incomeInvitation = user.testData().incomeInvitations().getFirst().username();

    final UserJson friend = gatewayApiClient.acceptInvitation(bearerToken, new FriendJson(
        incomeInvitation
    ));
    step("Check that response not null", () ->
        assertNotNull(friend)
    );

    step("Check friend in response", () -> {
      assertEquals(user.testData().incomeInvitations().getFirst().username(), friend.username());
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
    final String incomeInvitation = user.testData().incomeInvitations().getFirst().username();

    final UserJson declinedFriend = gatewayApiClient.declineInvitation(bearerToken, new FriendJson(
        incomeInvitation
    ));
    step("Check that response not null", () ->
        assertNotNull(declinedFriend)
    );

    step("Check declined friend in response", () -> {
      assertEquals(user.testData().incomeInvitations().getFirst().username(), declinedFriend.username());
      assertNull(declinedFriend.friendState());
    });

    step("Check that outcome & income invitations removed for both users", () ->
        Assertions.assertAll(
            () -> assertTrue(
                gatewayApiClient.allFriends(bearerToken, null).isEmpty(),
                "Current user should not have income invitations after declining"),
            () -> assertNull(
                userdataApiClient.allUsers(incomeInvitation, null).getFirst().friendState(),
                "Inviter should not have outcome invitations after declining")
        )
    );
    step("Check that friends request & income invitation removed for both users", () ->
        Assertions.assertAll(
            () -> assertTrue(
                gatewayApiClient.allFriends(bearerToken, null)
                    .isEmpty(),
                "Current user should not have income invitations after declining"),
            () -> assertNull(userdataApiClient.allUsers(incomeInvitation, null)
                    .getFirst()
                    .friendState(),
                "Inviter should not have outcome invitations after declining"),
            () -> assertTrue(
                userdataApiClient.friends(incomeInvitation, null)
                    .isEmpty(),
                "Inviter should not have friends after declining"
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
                FriendState.INVITE_SENT,
                gatewayApiClient.allUsers(bearerToken, null)
                    .getFirst()
                    .friendState(),
                "Current user should have outcome invitation after adding"),
            () -> assertEquals(
                FriendState.INVITE_RECEIVED,
                userdataApiClient.friends(friendWillBeAdded, null)
                    .getFirst()
                    .friendState(),
                "Target friend should have 1 income invitation"
            )
        )
    );
  }
}
