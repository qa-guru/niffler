package guru.qa.niffler.test.web;

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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Calendar;
import java.util.Date;

import static guru.qa.niffler.utils.DataUtils.randomAmount;
import static guru.qa.niffler.utils.DataUtils.randomSentence;
import static guru.qa.niffler.utils.ErrorMessage.CAN_NOT_CREATE_SPENDING_WITHOUT_AMOUNT;
import static guru.qa.niffler.utils.ErrorMessage.CAN_NOT_CREATE_SPENDING_WITHOUT_CATEGORY;
import static guru.qa.niffler.utils.ErrorMessage.CAN_NOT_CREATE_SPENDING_WITH_INCORRECT_DATE;
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
    double amount = randomAmount();
    Date currentDate = new Date();
    String description = randomSentence(3);
    SpendJson spendingToCheck = new SpendJson(
        null,
        currentDate,
        amount,
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

    new MainPage().getHeader()
        .addSpendingPage()
        .setNewSpendingCategory(category)
        .setNewSpendingAmount(amount)
        .selectSpendingDateInCalendar(currentDate)
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
    new MainPage().getHeader()
        .addSpendingPage()
        .setNewSpendingAmount(randomAmount())
        .selectSpendingDateInCalendar(new Date())
        .saveSpending()
        .checkFormErrorMessage(CAN_NOT_CREATE_SPENDING_WITHOUT_CATEGORY.content);
  }

  @ValueSource(doubles = {0, 0.001})
  @ParameterizedTest(name = "Нельзя добавить трату со значением суммы: {0}")
  @AllureId("500014")
  @DisplayName("WEB: Нельзя добавить трату с невалидным значением суммы")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser)
  void shouldNotAddSpendingWithIncorrectAmount(double amount) {
    new MainPage().getHeader()
        .addSpendingPage()
        .setNewSpendingCategory("Friends")
        .selectSpendingDateInCalendar(new Date())
        .setNewSpendingAmount(amount)
        .saveSpending()
        .checkFormErrorMessage(CAN_NOT_CREATE_SPENDING_WITHOUT_AMOUNT.content);
  }

  @ValueSource(strings = {"31/12/1969", ""})
  @ParameterizedTest(name = "Нельзя добавить трату со значением даты: {0}")
  @AllureId("500023")
  @DisplayName("WEB: Нельзя добавить трату с невалидным значением даты")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser)
  void shouldNotAddSpendingWithIncorrectDate(String date) {
    new MainPage().getHeader()
        .addSpendingPage()
        .setNewSpendingCategory("Friends")
        .pasteSpendingDate(date)
        .setNewSpendingAmount(randomAmount())
        .saveSpending()
        .checkFormErrorMessage(CAN_NOT_CREATE_SPENDING_WITH_INCORRECT_DATE.content);
  }

  @Test
  @AllureId("500015")
  @DisplayName("WEB: Пользователь имеет возможность отредактировать трату")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser(
      spends = @GenerateSpend(
          name = "Fish",
          category = "Food",
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
        randomSentence(3),
        null
    );

    SpendingTable spendingTable = new MainPage()
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
              name = "Коктейль 1",
              category = "Бар",
              addDaysToSpendDate = -7,
              amount = 650.0
          ),
          @GenerateSpend(
              name = "Коктейль 2",
              category = "Бар",
              addDaysToSpendDate = -8,
              amount = 650.0
          ),
      }
  ))
  void checkLastWeekSpendingTest(@User UserJson user) {
    new MainPage().getSpendingTable()
        .selectPeriod(DataFilterValues.WEEK)
        .checkTableContains(user.testData().spends().getFirst());
  }

  @Test
  @AllureId("500017")
  @DisplayName("WEB: Пользователь имеет возможность удалить трату")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser(
      spends = @GenerateSpend(
          name = "Коктейль",
          category = "Бар",
          amount = 650.0
      )
  ))
  void deleteSpendingTest() {
    new MainPage().getSpendingTable()
        .deleteSpending("Коктейль")
        .checkTableSize(0);
  }
}
