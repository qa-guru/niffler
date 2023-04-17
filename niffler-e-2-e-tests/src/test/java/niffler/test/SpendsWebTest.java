package niffler.test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import niffler.jupiter.GenerateSpend;
import niffler.jupiter.GenerateSpendExtension;
import niffler.model.CurrencyValues;
import niffler.model.SpendJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Disabled
@ExtendWith(GenerateSpendExtension.class)
public class SpendsWebTest {

    static {
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue("dima");
        $("input[name='password']").setValue("12345");
        $("button[type='submit']").click();
    }

    @GenerateSpend(
        username = "dima",
        description = "QA GURU ADVANCED VOL 2",
        currency = CurrencyValues.RUB,
        amount = 52000.00,
        category = "Обучение"
    )
    @Test
    void spendShouldBeDeletedByActionInTable(SpendJson spend) {
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
