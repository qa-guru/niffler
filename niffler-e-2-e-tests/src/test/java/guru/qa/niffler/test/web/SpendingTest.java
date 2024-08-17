package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.DataFilterValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.utils.DateUtils;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static guru.qa.niffler.utils.DataUtils.generateRandomSentence;
import static guru.qa.niffler.utils.ErrorMessage.CAN_NOT_CREATE_SPENDING_WITHOUT_AMOUNT;
import static guru.qa.niffler.utils.ErrorMessage.CAN_NOT_CREATE_SPENDING_WITHOUT_CATEGORY;
import static guru.qa.niffler.utils.SuccessMessage.SPENDING_ADDED;

@Epic(" [WEB][niffler-ng-client]: Траты")
@DisplayName(" [WEB][niffler-ng-client]: Траты")
public class SpendingTest extends BaseWebTest {

  @Test
  @AllureId("500011")
  @DisplayName("WEB: Пользователь имеет возможность добавить трату")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser)
  void shouldAddNewSpending(@User UserJson user) {
    String category = "Friends";
    int amount = 100;
    Date currentDate = new Date();
    String description = generateRandomSentence(3);
    SpendJson spendingToCheck = new SpendJson(
        null,
        currentDate,
        (double) amount,
        user.currency(),
        new CategoryJson(
            null,
            category,
            null,
            false
        ),
        description,
        null
    );

    Selenide.open(MainPage.URL, MainPage.class)
        .getHeader()
        .addSpendingPage()
        .setNewSpendingCategory(category)
        .setNewSpendingAmount(amount)
        .setNewSpendingDate(currentDate)
        .setNewSpendingDescription(description)
        .saveSpending()
        .checkAlertMessage(SPENDING_ADDED.content);

    new MainPage().getSpendingTable()
        .checkTableContains(spendingToCheck);
  }

  @Test
  @AllureId("500013")
  @DisplayName("WEB: Нельзя добавить трату пустой категорией")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser)
  void shouldNotAddSpendingWithEmptyCategory() {
    Selenide.open(MainPage.URL, MainPage.class)
        .getHeader()
        .addSpendingPage()
        .setNewSpendingAmount(100)
        .setNewSpendingDate(new Date())
        .saveSpending()
        .checkFormErrorMessage(CAN_NOT_CREATE_SPENDING_WITHOUT_CATEGORY.content);
  }

  @Test
  @AllureId("500014")
  @DisplayName("WEB: Нельзя добавить трату с пустым значением суммы")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser)
  void shouldNotAddSpendingWithEmptyAmount() {
    Selenide.open(MainPage.URL, MainPage.class)
        .getHeader()
        .addSpendingPage()
        .setNewSpendingCategory("Friends")
        .setNewSpendingDate(new Date())
        .saveSpending()
        .checkFormErrorMessage(CAN_NOT_CREATE_SPENDING_WITHOUT_AMOUNT.content);
  }

  @Test
  @AllureId("500015")
  @DisplayName("WEB: Пользователь имеет возможность отредактировать трату")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser(
      spends = @GenerateSpend(
          spendName = "Fish",
          spendCategory = "Food",
          amount = 1050.0,
          currency = CurrencyValues.USD
      )
  ))
  void shouldEditSpendingTest(@User UserJson user) {
    Date newSpendDate = DateUtils.addDaysToDate(new Date(), Calendar.DAY_OF_MONTH, -4);
    SpendJson testSpend = user.testData().spends().getFirst();
    SpendJson editedSpending = new SpendJson(
        null,
        newSpendDate,
        1000.0,
        CurrencyValues.RUB,
        new CategoryJson(
            null,
            "Friends",
            null,
            false
        ),
        generateRandomSentence(3),
        null
    );

    SpendingTable spendingTable = Selenide.open(MainPage.URL, MainPage.class)
        .getSpendingTable()
        .checkTableContains(testSpend);

    spendingTable.editSpending("Fish")
        .fillPage(editedSpending)
        .saveSpending();

    spendingTable.checkTableContains(editedSpending);
  }

  @Test
  @AllureId("500016")
  @DisplayName("WEB: Траты за последнюю неделю должны отображаться при применении фильтра 'Last week'")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser(
      spends = {
          @GenerateSpend(
              spendName = "Коктейль 1",
              spendCategory = "Бар",
              addDaysToSpendDate = -7,
              amount = 650.0
          ),
          @GenerateSpend(
              spendName = "Коктейль 2",
              spendCategory = "Бар",
              addDaysToSpendDate = -8,
              amount = 650.0
          ),
      }
  ))
  void checkLastWeekSpendingTest(@User UserJson user) {
    Selenide.open(MainPage.URL, MainPage.class)
        .getSpendingTable()
        .selectPeriod(DataFilterValues.WEEK)
        .checkTableContains(user.testData().spends().getFirst());
  }

  @Test
  @AllureId("500017")
  @DisplayName("WEB: Пользователь имеет возможность удалить трату")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser(
      spends = @GenerateSpend(
          spendName = "Коктейль",
          spendCategory = "Бар",
          amount = 650.0
      )
  ))
  void deleteSpendingTest() {
    Selenide.open(MainPage.URL, MainPage.class)
        .getSpendingTable()
        .deleteSpending("Коктейль")
        .checkTableSize(0);
  }
}
