package niffler.test.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import niffler.jupiter.annotation.ApiLogin;
import niffler.jupiter.annotation.GenerateCategory;
import niffler.jupiter.annotation.GenerateSpend;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.User;
import niffler.model.rest.SpendJson;
import niffler.model.rest.UserJson;
import niffler.page.MainPage;
import niffler.page.component.SpendingTable;
import niffler.utils.DateUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static niffler.utils.DataUtils.generateRandomSentence;
import static niffler.utils.Error.CAN_NOT_CREATE_SPENDING_WITHOUT_AMOUNT;
import static niffler.utils.Error.CAN_NOT_CREATE_SPENDING_WITHOUT_CATEGORY;
import static niffler.utils.Error.CAN_NOT_PICK_FUTURE_DATE_FOR_SPENDING;

@Epic("[WEB][niffler-frontend]: Траты")
@DisplayName("[WEB][niffler-frontend]: Траты")
public class SpendingTest extends BaseWebTest {

    private static final String ADD_SPENDING_SUCCESS_MSG = "Spending successfully added!";

    @Test
    @AllureId("500011")
    @DisplayName("WEB: Пользователь имеет возможность добавить трату")
    @Tag("WEB")
    @ApiLogin(nifflerUser = @GenerateUser(
            categories = @GenerateCategory("Friends")
    ))
    void shouldAddNewSpending(@User UserJson user) {
        String category = "Friends";
        int amount = 100;
        Date currentDate = new Date();
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
                .setNewSpendingDate(currentDate)
                .setNewSpendingDescription(description)
                .submitNewSpending()
                .checkToasterMessage(ADD_SPENDING_SUCCESS_MSG)
                .getSpendingTable()
                .checkTableContains(spendingToCheck);
    }

    @Test
    @AllureId("500012")
    @DisplayName("WEB: Нельзя добавить трату с будущей датой")
    @Tag("WEB")
    @ApiLogin(nifflerUser = @GenerateUser(
            categories = @GenerateCategory("Friends")
    ))
    void shouldNotAddSpendingWithFutureDate() {
        String category = "Friends";
        int amount = 100;
        Date spendDate = DateUtils.addDaysToDate(new Date(), Calendar.DAY_OF_MONTH, 2);
        String description = generateRandomSentence(3);

        Selenide.open(MainPage.URL, MainPage.class)
                .waitForPageLoaded()
                .setNewSpendingCategory(category)
                .setNewSpendingAmount(amount)
                .setNewSpendingDate(spendDate)
                .setNewSpendingDescription(description)
                .submitNewSpending()
                .checkError(CAN_NOT_PICK_FUTURE_DATE_FOR_SPENDING.content);
    }

    @Test
    @AllureId("500013")
    @DisplayName("WEB: Нельзя добавить трату пустой категорией")
    @Tag("WEB")
    @ApiLogin(nifflerUser = @GenerateUser(
            categories = @GenerateCategory("Friends")
    ))
    void shouldNotAddSpendingWithEmptyCategory() {
        int amount = 100;

        Selenide.open(MainPage.URL, MainPage.class)
                .waitForPageLoaded()
                .setNewSpendingAmount(amount)
                .setNewSpendingDate(new Date())
                .submitNewSpending()
                .checkError(CAN_NOT_CREATE_SPENDING_WITHOUT_CATEGORY.content);
    }

    @Test
    @AllureId("500014")
    @DisplayName("WEB: Нельзя добавить трату с пустым значением суммы")
    @Tag("WEB")
    @ApiLogin(nifflerUser = @GenerateUser(
            categories = @GenerateCategory("Friends")
    ))
    void shouldNotAddSpendingWithEmptyAmount() {
        String category = "Friends";

        Selenide.open(MainPage.URL, MainPage.class)
                .waitForPageLoaded()
                .setNewSpendingCategory(category)
                .setNewSpendingDate(new Date())
                .submitNewSpending()
                .checkError(CAN_NOT_CREATE_SPENDING_WITHOUT_AMOUNT.content);
    }

    @Test
    @AllureId("500015")
    @DisplayName("WEB: Пользователь имеет возможность отредактировать трату")
    @Tag("WEB")
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
    @AllureId("500016")
    @DisplayName("WEB: Траты за последнюю неделю должны отображаться при применении фильтра 'Last week'")
    @Tag("WEB")
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
    @AllureId("500017")
    @DisplayName("WEB: Пользователь имеет возможность удалить трату")
    @Tag("WEB")
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
                .deleteSpending();
    }
}
