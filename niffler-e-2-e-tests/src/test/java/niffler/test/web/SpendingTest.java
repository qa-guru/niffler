package niffler.test.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import niffler.jupiter.annotation.ApiLogin;
import niffler.model.CurrencyValues;
import niffler.model.SpendJson;
import niffler.page.MainPage;
import niffler.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.ResourceLocks;

@Execution(ExecutionMode.SAME_THREAD)
public class SpendingTest extends BaseTest {

    @Test
    @AllureId("50")
    @ResourceLocks({
            @ResourceLock("spend")
    })
    @ApiLogin(username = "dima", password = "12345")
    void checkLastWeekSpendingTest() throws Exception {
        SpendJson expected = new SpendJson();
        expected.setSpendDate(DateUtils.fromString("15 Feb 23"));
        expected.setAmount(800D);
        expected.setCurrency(CurrencyValues.USD);
        expected.setCategory("Бар");
        expected.setDescription("Коктейль");

        Selenide.open(MainPage.URL, MainPage.class)
                .getSpendingTable()
                .clickByButton("Last week")
                .checkTableContains(expected);
    }

    @Test
    @AllureId("51")
    @ResourceLock("spend")
    @ApiLogin(username = "dima", password = "12345")
    void deleteSpendingTest() {
        Selenide.open(MainPage.URL, MainPage.class)
                .getSpendingTable()
                .deleteSpending()
                .checkTable();
    }
}
