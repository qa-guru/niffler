package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.utils.DateUtils;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static guru.qa.niffler.utils.DataUtils.generateRandomSentence;
import static guru.qa.niffler.utils.ErrorMessage.CAN_NOT_CREATE_SPENDING_WITHOUT_AMOUNT;
import static guru.qa.niffler.utils.ErrorMessage.CAN_NOT_CREATE_SPENDING_WITHOUT_CATEGORY;
import static guru.qa.niffler.utils.ErrorMessage.CAN_NOT_PICK_FUTURE_DATE_FOR_SPENDING;
import static guru.qa.niffler.utils.SuccessMessage.SPENDING_ADDED;

@Epic("[WEB][niffler-frontend]: Траты")
@DisplayName("[WEB][niffler-frontend]: Траты")
public class SpendingTest extends BaseWebTest {

    @Test
    @AllureId("500011")
    @DisplayName("WEB: Пользователь имеет возможность добавить трату")
    @Tag("WEB")
    @ApiLogin(user = @GenerateUser(
            categories = @GenerateCategory("Friends")
    ))
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
                category,
                description,
                null
        );

        Selenide.open(MainPage.URL, MainPage.class)
                .waitForPageLoaded()
                .setNewSpendingCategory(category)
                .setNewSpendingAmount(amount)
                .setNewSpendingDate(currentDate)
                .setNewSpendingDescription(description)
                .submitNewSpending()
                .checkToasterMessage(SPENDING_ADDED.content)
                .getSpendingTable()
                .checkTableContains(spendingToCheck);
    }

    @Test
    @AllureId("500012")
    @DisplayName("WEB: Нельзя добавить трату с будущей датой")
    @Tag("WEB")
    @ApiLogin(user = @GenerateUser(
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
    @ApiLogin(user = @GenerateUser(
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
    @ApiLogin(user = @GenerateUser(
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
    @ApiLogin(user = @GenerateUser(
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
        SpendJson testSpend = user.testData().spends().get(0);
        SpendJson editedSpending = new SpendJson(
                null,
                testSpend.spendDate(),
                1000.0,
                testSpend.currency(),
                "Friends",
                generateRandomSentence(3),
                null
        );

        SpendingTable spendingTable = Selenide.open(MainPage.URL, MainPage.class)
                .waitForPageLoaded()
                .getSpendingTable()
                .checkTableContains(testSpend)
                .editSpending(0, editedSpending);

        Selenide.refresh();

        spendingTable.checkTableContains(editedSpending);
    }

    @Test
    @AllureId("500016")
    @DisplayName("WEB: Траты за последнюю неделю должны отображаться при применении фильтра 'Last week'")
    @Tag("WEB")
    @ApiLogin(user = @GenerateUser(
            categories = @GenerateCategory("Бар"),
            spends = @GenerateSpend(
                    spendName = "Коктейль",
                    spendCategory = "Бар",
                    addDaysToSpendDate = -5,
                    amount = 650.0
            )
    ))
    void checkLastWeekSpendingTest(@User UserJson user) {
        Selenide.open(MainPage.URL, MainPage.class)
                .getSpendingTable()
                .clickByButton("Last week")
                .checkTableContains(user.testData().spends().get(0));
    }

    @Disabled("Not implemented")
    @Test
    @AllureId("500017")
    @DisplayName("WEB: Пользователь имеет возможность удалить трату")
    @Tag("WEB")
    @ApiLogin(user = @GenerateUser(
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
