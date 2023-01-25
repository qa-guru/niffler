package niffler.tests;

import niffler.api.spend.dto.SpendDto;
import niffler.config.AppProperties;
import niffler.jupiter.spend.WithSpend;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static niffler.models.Category.*;

class HistoryOfSpendingTest extends BaseTest {

    @WithSpend(amount = 2_500.0, username = "sashkir7")
    private SpendDto restaurantSpend;

    @WithSpend(category = PRODUCTS, amount = 1_000.0, description = "Ашан", username = "sashkir7")
    private SpendDto productSpend;

    @BeforeEach
    void login() {
        // ToDo to page object
        open(AppProperties.FRONTEND_APP_URL);
        $("a[href='/redirect']").click();
        $("[name=username]").setValue("sashkir7");
        $("[name=password]").setValue("12345");
        $("[type=submit]").click();
        $("h1").shouldHave(text("Niffler. The coin keeper."));
    }

    @Test
    void historyOfSpendingTableContainsSpending() {
        checkSpendingTableContainsSpending(restaurantSpend);
        checkSpendingTableContainsSpending(productSpend);
    }

    private void checkSpendingTableContainsSpending(SpendDto spend) {
        // ToDo to page object
        $(".spendings-table").shouldHave(
                text(spend.getDateInSpendingTableStyle()),
                text(spend.getAmountInSpendingTableStyle()),
                text(spend.getCurrency().toString()),
                text(spend.getCategory().toString()),
                text(spend.getDescription()));
    }

}
