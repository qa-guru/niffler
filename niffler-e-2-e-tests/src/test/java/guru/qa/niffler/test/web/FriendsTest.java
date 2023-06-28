package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Friends;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.IncomeInvitations;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.utils.SuccessMessage;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.extension.CreateUserExtension.Selector.METHOD;
import static guru.qa.niffler.jupiter.extension.CreateUserExtension.Selector.NESTED;


@Epic("[WEB][niffler-frontend]: Друзья")
@DisplayName("[WEB][niffler-frontend]: Друзья")
public class FriendsTest extends BaseWebTest {

    @Test
    @AllureId("500018")
    @DisplayName("WEB: Пользователь должен видеть список своих друзей")
    @Tag("WEB")
    @ApiLogin(nifflerUser = @GenerateUser(friends = @Friends(count = 2)))
    void shouldViewExistingFriendsInTable(@User UserJson user) {
        Selenide.open(MainPage.URL, MainPage.class)
                .getHeader()
                .toFriendsPage()
                .checkExistingFriends(user.getFriendsJsons());
    }

    @Test
    @AllureId("500019")
    @DisplayName("WEB: Пользователь имеет возможность отправить запрос на добавление в друзья")
    @Tag("WEB")
    @ApiLogin(nifflerUser = @GenerateUser)
    @GenerateUser()
    void shouldSendInvitation(@User(selector = NESTED) UserJson currentUser,
                              @User(selector = METHOD) UserJson userToSendInvitation) {
        PeoplePage peoplePage = Selenide.open(PeoplePage.URL, PeoplePage.class)
                .waitForPageLoaded()
                .sendFriendInvitationToUser(userToSendInvitation.getUsername());

        Selenide.refresh();

        peoplePage.checkInvitationSentToUser(userToSendInvitation.getUsername());
    }

    @Test
    @AllureId("500020")
    @DisplayName("WEB: Пользователь имеет возможность удалить пользователя из друзей")
    @Tag("WEB")
    @ApiLogin(nifflerUser = @GenerateUser(friends = @Friends(count = 2)))
    void shouldRemoveFriend(@User(selector = NESTED) UserJson user) {
        UserJson userToRemove = user.getFriendsJsons().remove(0);
        FriendsPage friendsPage = Selenide.open(FriendsPage.URL, FriendsPage.class)
                .waitForPageLoaded()
                .removeFriend(userToRemove.getUsername())
                .checkToasterMessage(SuccessMessage.FRIEND_DELETED.content);

        Selenide.refresh();

        friendsPage.checkExistingFriends(user.getFriendsJsons());
    }

    @Test
    @AllureId("500021")
    @DisplayName("WEB: Пользователь должен иметь возможность принять приглашение в друзья")
    @Tag("WEB")
    @ApiLogin(nifflerUser = @GenerateUser(incomeInvitations = @IncomeInvitations(count = 2)))
    void shouldAcceptInvitation(@User UserJson user) {
        UserJson userToAcceptInvitation = user.getInvitationsJsons().remove(0);
        user.getFriendsJsons().add(userToAcceptInvitation);
        FriendsPage friendsPage = Selenide.open(FriendsPage.URL, FriendsPage.class)
                .waitForPageLoaded()
                .acceptFriendInvitationFromUser(userToAcceptInvitation.getUsername())
                .checkToasterMessage(SuccessMessage.INVITATION_ACCEPTED.content);

        Selenide.refresh();

        friendsPage.checkExistingFriends(user.getFriendsJsons());
    }

    @Test
    @AllureId("500022")
    @DisplayName("WEB: Пользователь должен иметь возможность отклонить приглашение в друзья")
    @Tag("WEB")
    @ApiLogin(nifflerUser = @GenerateUser(incomeInvitations = @IncomeInvitations(count = 2)))
    void shouldDeclineInvitation(@User UserJson user) {
        UserJson userToDeclineInvitation = user.getInvitationsJsons().get(0);
        FriendsPage friendsPage = Selenide.open(FriendsPage.URL, FriendsPage.class)
                .waitForPageLoaded()
                .removeFriend(userToDeclineInvitation.getUsername())
                .checkToasterMessage(SuccessMessage.INVITATION_DECLINED.content);

        Selenide.refresh();

        friendsPage.checkExistingFriends(user.getFriendsJsons());
    }
}
