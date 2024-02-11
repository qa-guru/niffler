package guru.qa.niffler.test.web.other;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

import guru.qa.niffler.jupiter.annotation.ApiRegisterWithRandomUser;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.component.CategoryRustamComponent;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

public class CustomCondision7Test extends BaseWebTest {

    final String USERNAME = "test37";
    final String CATEGORY_NAME = "обучение4";
    CategoryRustamComponent categoryRustamComponent = new CategoryRustamComponent();

    @ApiRegisterWithRandomUser(username = USERNAME, password = "12345", submitPassword = "12345")
//    @ApiRegisterWithRandomUser(randomUser = @GenerateRandomUser)
//    @GenerateSpend(
//        username = USERNAME,
//        description = "QA GURU ADVANCED VOL 1",
//        randomCategory = @GenerateCategory(
//            username = USERNAME,
//            category = CATEGORY_NAME
//        ),
//        amount = 52000.00,
//        currency = CurrencyValues.RUB
//    )
    @GenerateCategory(
        username = USERNAME,
        category = CATEGORY_NAME
    )
    @AllureId("60")
    @Test
    void checkLastWeekSpendingWithRandomUserTest(UserJson userJson, CategoryJson categoryJson) {
        System.out.println("Это айдишник нового юзера: " + userJson.getId());

        open(CFG.getFrontUrl() + "/main");

        $("input#react-select-3-input").setValue("обучение").pressEnter();
        $("[placeholder='Set Amount']").setValue("520");
        $("form button.button").click();

//        $(".spendings-table tbody").$$("tr")
//            .find(text(categoryJson.getCategory()))
//            .$$("td").first()
//            .scrollTo()
//            .click();

        categoryRustamComponent.checkTableContainsCategory(categoryJson);

//        $$(".button_type_small").find(text("Delete selected"))
//            .click();
//
//        $(".spendings-table tbody")
//            .$$("tr")
//            .shouldHave(CollectionCondition.size(0));
    }

}
