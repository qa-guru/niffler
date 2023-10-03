package guru.qa.niffler.test;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.enums.CurrencyValues;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.extension.GenerateSpendExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Disabled
public class SpendsWebTest extends BaseWebTest {

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue("rustam");
        $("input[name='password']").setValue("12345");
        $("button[type='submit']").click();
    }

    @GenerateCategory(
          category = "обучение",
          username = "rustam"
    )
    @GenerateSpend(
          username = "rustam",
          description = "QA GURU ADVANCED VOL 1",
          category = "обучение",
          amount = 52000.00,
          currency = CurrencyValues.RUB
    )
    @Test
    void spendShouldBeDeletedByActionInTable(CategoryJson category, SpendJson spend) {

        $(".spendings-table tbody").$$("tr")
              .find(text(spend.getDescription()))
              .$$("td").first()
              .scrollTo()
              .click();

        $$(".button_type_small").find(text("Delete selected"))
              .click();

        $(".spendings-table tbody").$$("tr")
              .shouldHave(CollectionCondition.size(0));
        throw new IllegalStateException();
    }
}
