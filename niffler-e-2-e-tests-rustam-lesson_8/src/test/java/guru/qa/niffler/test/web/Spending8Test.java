package guru.qa.niffler.test.web;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

import com.codeborne.selenide.CollectionCondition;
import guru.qa.niffler.db.entity.userdata.CurrencyValues;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.Spend7Json;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

public class Spending8Test extends BaseWebTest {

    @GenerateSpend(
        username = "rustam",
        description = "QA GURU ADVANCED VOL 1",
        category = "обучение",
        amount = 52000.00,
        currency = CurrencyValues.RUB
    )
    @ApiLogin(username = "rustam", password = "12345")
    @AllureId("50")
    @Test
    void checkLastWeekSpendingTest(Spend7Json spend) {
        open(CFG.getFrontUrl() + "/main");

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

}
