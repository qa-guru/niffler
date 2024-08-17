package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Friends;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.IncomeInvitations;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.PeoplePage;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.jupiter.annotation.User.Selector.METHOD;
import static guru.qa.niffler.jupiter.annotation.User.Selector.NESTED;


@Epic(" [WEB][niffler-ng-client]: Друзья")
@DisplayName(" [WEB][niffler-ng-client]: Друзья")
public class FriendsTest extends BaseWebTest {

  @Test
  @AllureId("500018")
  @DisplayName("WEB: Пользователь должен видеть список своих друзей")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser(friends = @Friends(count = 2)))
  void shouldViewExistingFriendsInTable(@User UserJson user) {
    final String[] expectedFriends = user.testData().friendsUsernames();
    open(FriendsPage.URL, FriendsPage.class)
        .checkExistingFriends(expectedFriends);
  }

  @Test
  @AllureId("500019")
  @DisplayName("WEB: Пользователь имеет возможность отправить запрос на добавление в друзья")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser)
  @GenerateUser
  void shouldSendInvitation(@User(selector = METHOD) UserJson userToSendInvitation) {
    open(PeoplePage.URL, PeoplePage.class)
        .sendFriendInvitationToUser(userToSendInvitation.username())
        .checkInvitationSentToUser(userToSendInvitation.username());
  }

  @Test
  @AllureId("500020")
  @DisplayName("WEB: Пользователь имеет возможность удалить пользователя из друзей")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser(friends = @Friends(count = 2)))
  void shouldRemoveFriend(@User(selector = NESTED) UserJson user) {
    UserJson userToRemove = user.testData().friends().removeFirst();
    open(FriendsPage.URL, FriendsPage.class)
        .removeFriend(userToRemove.username())
        .checkExistingFriends(user.testData().friendsUsernames());
  }

  @Test
  @AllureId("500021")
  @DisplayName("WEB: Пользователь должен иметь возможность принять приглашение в друзья")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser(incomeInvitations = @IncomeInvitations(count = 1)))
  void shouldAcceptInvitation(@User UserJson user) {
    UserJson userToAcceptInvitation = user.testData().incomeInvitations().removeFirst();

    FriendsPage friendsPage = open(FriendsPage.URL, FriendsPage.class)
        .checkExistingInvitationsCount(1)
        .acceptFriendInvitationFromUser(userToAcceptInvitation.username());

    friendsPage.checkExistingInvitationsCount(0);
    friendsPage.checkExistingFriendsCount(1);
    friendsPage.checkExistingFriends(userToAcceptInvitation.username());
  }

  @Test
  @AllureId("500022")
  @DisplayName("WEB: Пользователь должен иметь возможность отклонить приглашение в друзья")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser(incomeInvitations = @IncomeInvitations(count = 1)))
  void shouldDeclineInvitation(@User UserJson user) {
    UserJson userToDeclineInvitation = user.testData().incomeInvitations().getFirst();
    FriendsPage friendsPage = open(FriendsPage.URL, FriendsPage.class)
        .waitForPageLoaded()
        .checkExistingInvitationsCount(1)
        .declineFriendInvitationFromUser(userToDeclineInvitation.username());

    friendsPage.checkExistingInvitationsCount(0);
    friendsPage.checkExistingFriendsCount(0);

    open(PeoplePage.URL, PeoplePage.class)
        .checkExistingUser(userToDeclineInvitation.username());
  }
}
