package guru.qa.niffler.test.web;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.GenerateUserInAuthDb;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.condition.SpendCondition.spends;

@Disabled
public class SpendsWebTest extends BaseWebTest {

    private static final String TEST_USERNAME = "dima";
    private static final String TEST_PWD = "12345";

    @GenerateUserInAuthDb(username = TEST_USERNAME, password = TEST_PWD)
    @GenerateSpend(
            username = TEST_USERNAME,
            description = "QA GURU ADVANCED VOL 2",
            currency = CurrencyValues.RUB,
            amount = 52000.00,
            category = "Обучение"
    )
    @ApiLogin(username = TEST_USERNAME, password = TEST_PWD)
    @AllureId("101")
    @Test
    void spendShouldBeDeletedByActionInTable(UserJson user, SpendJson spend) {
        Selenide.open(CFG.getFrontUrl() + "/main");

        $(".spendings-table tbody").$$("tr")
                .find(text(spend.getDescription()))
                .$$("td").first()
                .scrollTo()
                .click();

        $$(".button_type_small").find(text("Delete selected"))
                .click();

        $(".spendings-table tbody")
                .$$("tr")
                .shouldHave(CollectionCondition.size(0));
    }

    @GenerateUserInAuthDb(username = TEST_USERNAME, password = TEST_PWD)
    @GenerateSpend(
            username = "dima",
            description = "QA GURU ADVANCED VOL 2",
            currency = CurrencyValues.RUB,
            amount = 52000.00,
            category = "Обучение"
    )
    @ApiLogin(username = "dima", password = "12345")
    @AllureId("101")
    @Test
    void spendInTableShouldBeEqualToGiven(SpendJson spend) {
        Selenide.open(CFG.getFrontUrl() + "/main");

        $(".spendings-table tbody")
                .$$("tr")
                .shouldHave(spends(spend));
    }
}
