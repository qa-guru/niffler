package niffler.test.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import niffler.jupiter.annotation.ApiLogin;
import niffler.jupiter.annotation.GenerateCategory;
import niffler.jupiter.annotation.GenerateSpend;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.User;
import niffler.model.UserJson;
import niffler.page.MainPage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;


@Execution(ExecutionMode.SAME_THREAD)
public class SpendingTest extends BaseTest {

    @Test
    @DisplayName("WEB: Траты за последнюю неделю должны отображаться при применении фильтра 'Last week'")
    @AllureId("50")
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
    @DisplayName("WEB: После удаления тарты она не должна отображаться в таблице")
    @AllureId("51")
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
