package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Friends;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.PeoplePage;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.jupiter.annotation.User.Selector.METHOD;

@Epic(" [WEB][niffler-ng-client]: Динамический поиск")
@DisplayName(" [WEB][niffler-ng-client]: Динамический поиск")
public class DynamicSearchTest extends BaseWebTest {

  @Test
  @AllureId("500029")
  @DisplayName("WEB: Динамический поиск трат по описанию без нажатия Enter")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser(
      spends = {
          @GenerateSpend(
              name = "Кофе латте",
              category = "Кафе",
              amount = 350.0
          ),
          @GenerateSpend(
              name = "Бензин АИ-95",
              category = "Транспорт",
              amount = 2500.0
          ),
      }
  ))
  void shouldFilterSpendingsByDynamicSearch() {
    new MainPage().getSpendingTable()
        .searchSpendingByDescription("Кофе")
        .checkTableSize(1);

    new MainPage().getSpendingTable()
        .searchSpendingByDescription("Бензин")
        .checkTableSize(1);
  }

  @Test
  @AllureId("500030")
  @DisplayName("WEB: Динамический поиск пользователей в таблице All People без нажатия Enter")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser(friends = @Friends(count = 2)))
  @GenerateUser
  void shouldFilterPeopleByDynamicSearch(@User(selector = METHOD) UserJson anotherUser) {
    open(PeoplePage.URL, PeoplePage.class)
        .searchPeople(anotherUser.username())
        .checkPeopleTableSize(1);
  }

  @Test
  @AllureId("500031")
  @DisplayName("WEB: Динамический поиск друзей без нажатия Enter")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser(friends = @Friends(count = 2)))
  void shouldFilterFriendsByDynamicSearch(@User UserJson user) {
    String firstFriend = user.testData().friends().getFirst().username();

    open(FriendsPage.URL, FriendsPage.class)
        .searchFriend(firstFriend)
        .checkExistingFriendsCount(1)
        .checkExistingFriends(firstFriend);
  }

  @Test
  @AllureId("500032")
  @DisplayName("WEB: Очистка поиска трат возвращает все результаты")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser(
      spends = {
          @GenerateSpend(
              name = "Кофе латте",
              category = "Кафе",
              amount = 350.0
          ),
          @GenerateSpend(
              name = "Бензин АИ-95",
              category = "Транспорт",
              amount = 2500.0
          ),
      }
  ))
  void shouldShowAllSpendsAfterClearingSearch() {
    new MainPage().getSpendingTable()
        .searchSpendingByDescription("Кофе")
        .checkTableSize(1)
        .searchSpendingByDescription("")
        .checkTableSize(2);
  }
}
