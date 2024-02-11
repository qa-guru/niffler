package guru.qa.niffler.test.web.other;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

import com.codeborne.selenide.CollectionCondition;
import guru.qa.niffler.db.entity.userdata.CurrencyValues;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.ApiRegisterWithRandomUser;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateRandomUser;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.Spend7Json;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

public class DBUser7Test extends BaseWebTest {

    final String USERNAME = "test31";

//    @ApiRegister(username = "test12", password = "12345", submitPassword = "12345")
    @DBUser(username = USERNAME, password = "12345", submitPassword = "12345")
    @ApiLogin(username = USERNAME, password = "12345")
    @GenerateCategory(
        username = USERNAME,
        category = "обучение"
    )
    @GenerateSpend(
        username = USERNAME,
        description = "QA GURU ADVANCED VOL 1",
        category = "обучение",
        amount = 52000.00,
        currency = CurrencyValues.RUB
    )
    @AllureId("55")
    @Test
    void checkLastWeekSpendingTest(UserJson userJson, Spend7Json spend) {
        System.out.println("Это айдишник нового юзера: " + userJson.getId());

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

//    @DBUser(username = USERNAME, password = "12345", submitPassword = "12345")
//    @ApiLogin(username = USERNAME, password = "12345")
    @ApiRegisterWithRandomUser(username = USERNAME, password = "12345", submitPassword = "12345")
//    @ApiRegisterWithRandomUser(randomUser = @GenerateRandomUser)
//    @GenerateCategory(
//        username = USERNAME,
//        category = "обучение"
//    )
    @GenerateSpend(
        username = USERNAME,
        description = "QA GURU ADVANCED VOL 1",
//        category = "обучение",
        randomCategory = @GenerateCategory(
            username = USERNAME,
            category = "обучение2"
        ),
        amount = 52000.00,
        currency = CurrencyValues.RUB
    )
    @AllureId("56")
    @Test
    void checkLastWeekSpendingWithRandomUserTest(UserJson userJson, Spend7Json spend) {
        System.out.println("Это айдишник нового юзера: " + userJson.getId());

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

    @GenerateRandomUser
    @AllureId("57")
    @Test
    void checkLastWeekSpendingWithRandomUserLikeAnnotationTest(UserJson userJson) {
        System.out.println("Это айдишник нового юзера: " + userJson.getId());

        open(CFG.getFrontUrl() + "/main");
    }
}
