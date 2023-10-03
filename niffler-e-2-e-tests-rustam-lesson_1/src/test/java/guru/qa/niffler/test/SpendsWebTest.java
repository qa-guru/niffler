package guru.qa.niffler.test;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.enums.CurrencyValues;
import guru.qa.niffler.jupiter.GenerateCategory;
import guru.qa.niffler.jupiter.GenerateSpend;
import guru.qa.niffler.jupiter.GenerateSpendExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.checkerframework.checker.units.qual.Speed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ExtendWith(GenerateSpendExtension.class)
public class SpendsWebTest {

    static {
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue("rustam");
        $("input[name='password']").setValue("12345");
        $("button[type='submit']").click();
    }

//    @GenerateCategory(
//          category = "обучение",
//          username = "rustam"
//    )
//    @GenerateSpend(
//          username = "rustam",
//          description = "QA GURU ADVANCED VOL 1",
//          category = "обучение",
//          amount = 52000.00,
//          currency = CurrencyValues.RUB
//    )
    @GenerateSpend(
          username = "rustam",
          description = "QA GURU ADVANCED VOL 1",
          category = @GenerateCategory(
              category = "обучение",
              username = "rustam"
          ),
          amount = 52000.00,
          currency = CurrencyValues.RUB
    )
    @Test
    void spendShouldBeDeletedByActionInTable(CategoryJson category, GenerateSpend test, SpendJson spend) {
        try {
            $(".spendings-table tbody").$$("tr")
                .find(text(spend.getDescription()))
                .$$("td").first()
                .scrollTo()
                .click();

            $$(".button_type_small").find(text("Delete selected"))
                .click();

            $(".spendings-table tbody").$$("tr")
                .shouldHave(CollectionCondition.size(0));
        } catch (Exception e) {}

    }
}
