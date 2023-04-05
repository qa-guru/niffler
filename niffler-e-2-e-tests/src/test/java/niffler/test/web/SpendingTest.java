package niffler.test.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import niffler.jupiter.annotation.ApiLogin;
import niffler.jupiter.annotation.GenerateCategory;
import niffler.jupiter.annotation.GenerateSpend;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.User;
import niffler.model.SpendJson;
import niffler.model.UserJson;
import niffler.page.MainPage;
import niffler.page.component.SpendingTable;
import niffler.utils.DateUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import java.util.Calendar;
import java.util.Date;

import static niffler.utils.DataUtils.generateRandomSentence;
import static niffler.utils.Error.CAN_NOT_CREATE_SPENDING_WITHOUT_AMOUNT;
import static niffler.utils.Error.CAN_NOT_CREATE_SPENDING_WITHOUT_CATEGORY;
import static niffler.utils.Error.CAN_NOT_PICK_FUTURE_DATE_FOR_SPENDING;


@Execution(ExecutionMode.SAME_THREAD)
public class SpendingTest extends BaseTest {

    private static final String ADD_SPENDING_SUCCESS_MSG = "Spending successfully added!";

    @Test
    @AllureId("40")
    @DisplayName("WEB: Пользователь имеет возможность добавить трату")
    @ApiLogin(nifflerUser = @GenerateUser(
            categories = @GenerateCategory("Friends")
    ))
    void shouldAddNewSpending(@User UserJson user) {
        String category = "Friends";
        int amount = 100;
        Date currentDate = new Date();
        String date = DateUtils.getDateAsString(currentDate);
        String description = generateRandomSentence(3);
        SpendJson spendingToCheck = new SpendJson();
        spendingToCheck.setCurrency(user.getCurrency());
        spendingToCheck.setSpendDate(currentDate);
        spendingToCheck.setCategory(category);
        spendingToCheck.setDescription(description);
        spendingToCheck.setAmount(Double.valueOf(amount));


        Selenide.open(MainPage.URL, MainPage.class)
                .waitForPageLoaded()
                .setNewSpendingCategory(category)
                .setNewSpendingAmount(amount)
                .setNewSpendingDate(date)
                .setNewSpendingDescription(description)
                .submitNewSpending()
                .checkToasterMessage(ADD_SPENDING_SUCCESS_MSG)
                .getSpendingTable()
                .checkTableContains(spendingToCheck);
    }

    @Test
    @AllureId("41")
    @DisplayName("WEB: Нельзя добавить трату с будущей датой")
    @ApiLogin(nifflerUser = @GenerateUser(
            categories = @GenerateCategory("Friends")
    ))
    void shouldNotAddSpendingWithFutureDate(@User UserJson user) {
        String category = "Friends";
        int amount = 100;
        Date spendDate = DateUtils.addDaysToDate(new Date(), Calendar.DAY_OF_MONTH, 2);
        String formattedDate = DateUtils.getDateAsString(spendDate);
        String description = generateRandomSentence(3);

        Selenide.open(MainPage.URL, MainPage.class)
                .waitForPageLoaded()
                .setNewSpendingCategory(category)
                .setNewSpendingAmount(amount)
                .setNewSpendingDate(formattedDate)
                .setNewSpendingDescription(description)
                .submitNewSpending()
                .checkError(CAN_NOT_PICK_FUTURE_DATE_FOR_SPENDING.content);
    }

    @Test
    @AllureId("42")
    @DisplayName("WEB: Нельзя добавить трату пустой категорией")
    @ApiLogin(nifflerUser = @GenerateUser(
            categories = @GenerateCategory("Friends")
    ))
    void shouldNotAddSpendingWithEmptyCategory(@User UserJson user) {
        int amount = 100;
        String date = DateUtils.getDateAsString(new Date());

        Selenide.open(MainPage.URL, MainPage.class)
                .waitForPageLoaded()
                .setNewSpendingAmount(amount)
                .setNewSpendingDate(date)
                .submitNewSpending()
                .checkError(CAN_NOT_CREATE_SPENDING_WITHOUT_CATEGORY.content);
    }

    @Test
    @AllureId("42")
    @DisplayName("WEB: Нельзя добавить трату с пустым значением суммы")
    @ApiLogin(nifflerUser = @GenerateUser(
            categories = @GenerateCategory("Friends")
    ))
    void shouldNotAddSpendingWithEmptyAmount(@User UserJson user) {
        String category = "Friends";
        String date = DateUtils.getDateAsString(new Date());

        Selenide.open(MainPage.URL, MainPage.class)
                .waitForPageLoaded()
                .setNewSpendingCategory(category)
                .setNewSpendingDate(date)
                .submitNewSpending()
                .checkError(CAN_NOT_CREATE_SPENDING_WITHOUT_AMOUNT.content);
    }

    @Test
    @AllureId("43")
    @DisplayName("WEB: Пользователь имеет возможность отредактировать трату")
    @ApiLogin(nifflerUser = @GenerateUser(
            categories = {
                    @GenerateCategory("Food"),
                    @GenerateCategory("Friends")
            },
            spends = @GenerateSpend(
                    spendName = "Fish",
                    spendCategory = "Food",
                    amount = 1050.0
            )
    ))
    void shouldEditSpendingTest(@User UserJson user) {
        Date newSpendDate = DateUtils.addDaysToDate(new Date(), Calendar.DAY_OF_MONTH, -4);
        SpendJson editedSpending = new SpendJson();
        editedSpending.setDescription(generateRandomSentence(3));
        editedSpending.setAmount(1000.0);
        editedSpending.setCurrency(user.getSpendJsons().get(0).getCurrency());
        editedSpending.setCategory("Friends");
        editedSpending.setSpendDate(user.getSpendJsons().get(0).getSpendDate());

        SpendingTable spendingTable = Selenide.open(MainPage.URL, MainPage.class)
                .waitForPageLoaded()
                .getSpendingTable()
                .checkTableContains(user.getSpendJsons().get(0))
                .editSpending(0, editedSpending);

        Selenide.refresh();

        spendingTable.checkTableContains(editedSpending);
    }

    @Test
    @AllureId("50")
    @DisplayName("WEB: Траты за последнюю неделю должны отображаться при применении фильтра 'Last week'")
    @ApiLogin(nifflerUser = @GenerateUser(
            categories = @GenerateCategory("Бар"),
            spends = @GenerateSpend(
                    spendName = "Коктейль",
                    spendCategory = "Бар",
                    addDaysToSpendDate = -5,
                    amount = 650.0
            )
    ))
    void checkLastWeekSpendingTest(@User UserJson user) throws Exception {
        Selenide.open(MainPage.URL, MainPage.class)
                .getSpendingTable()
                .clickByButton("Last week")
                .checkTableContains(user.getSpendJsons().get(0));
    }

    @Disabled("Not implemented")
    @Test
    @AllureId("51")
    @DisplayName("WEB: Пользователь имеет возможность удалить трату")
    @ApiLogin(nifflerUser = @GenerateUser(
            categories = @GenerateCategory("Бар"),
            spends = @GenerateSpend(
                    spendName = "Коктейль",
                    spendCategory = "Бар",
                    amount = 650.0
            )
    ))
    void deleteSpendingTest() {
        Selenide.open(MainPage.URL, MainPage.class)
                .getSpendingTable()
                .deleteSpending()
                .checkTable();
    }
}
