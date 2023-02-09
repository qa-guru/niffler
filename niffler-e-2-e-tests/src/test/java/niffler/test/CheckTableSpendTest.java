package niffler.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import io.qameta.allure.TmsLink;
import niffler.jupiter.BeforeCheckSpendTable;
import niffler.model.SpendJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static niffler.jupiter.BeforeCheckSpendTable.requestList;

@ExtendWith(BeforeCheckSpendTable.class)
public class CheckTableSpendTest {

    @TmsLink("123")
    @Test
    void CheckTableTest() throws IOException {
        Selenide.open("http://127.0.0.1:3000/");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue("dima");
        $("input[name='password']").setValue("123");
        $("button[type='submit']").click();
        $(".header__title").shouldBe(visible)
                .shouldHave(text("Niffler. The coin keeper."));

        URL url = Resources.getResource(requestList.get(0));
        String text = Resources.toString(url, UTF_8);
        SpendJson spend = new Gson().fromJson(text, SpendJson.class);

        SimpleDateFormat newDateFormat = new SimpleDateFormat("d MMM yy", Locale.ENGLISH);
        String result = newDateFormat.format(spend.getSpendDate());

        $x("//table[contains(@class,'table spendings-table')]/tbody[1]/tr[1]/td[2]")
                .shouldBe(visible).shouldHave(text(result));
        $x("//table[contains(@class,'table spendings-table')]/tbody[1]/tr[1]/td[3]")
                .shouldBe(visible).shouldHave(text(spend.getAmount().toString()));
        $x("//table[contains(@class,'table spendings-table')]/tbody[1]/tr[1]/td[4]")
                .shouldBe(visible).shouldHave(text(spend.getCurrency().toString()));
        $x("//table[contains(@class,'table spendings-table')]/tbody[1]/tr[1]/td[5]")
                .shouldBe(visible).shouldHave(text(spend.getCategory()));
        $x("//table[contains(@class,'table spendings-table')]/tbody[1]/tr[1]/td[6]")
                .shouldBe(visible).shouldHave(text(spend.getDescription()));


    }
}
