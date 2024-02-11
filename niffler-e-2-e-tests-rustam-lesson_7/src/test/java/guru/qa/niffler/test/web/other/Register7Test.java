package guru.qa.niffler.test.web.other;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

import com.codeborne.selenide.CollectionCondition;
import guru.qa.niffler.db.entity.userdata.CurrencyValues;
import guru.qa.niffler.jupiter.annotation.ApiRegister;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.Spend7Json;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

public class Register7Test extends BaseWebTest {

    @ApiRegister(username = "test12", password = "12345", submitPassword = "12345")
//    @ApiLogin(username = "test11", password = "12345")
    @GenerateCategory(
        username = "test12",
        category = "обучение"
    )
    @GenerateSpend(
        username = "test12",
        description = "QA GURU ADVANCED VOL 1",
        category = "обучение",
        amount = 52000.00,
        currency = CurrencyValues.RUB
    )
    @AllureId("51")
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
