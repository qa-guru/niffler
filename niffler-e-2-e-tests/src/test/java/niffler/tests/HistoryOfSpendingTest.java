package niffler.tests;

import niffler.api.base.spend.dto.SpendDto;
import niffler.jupiter.spend.WithSpend;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static niffler.models.Category.PRODUCTS;

class HistoryOfSpendingTest extends BaseTest {

    @WithSpend(amount = 1500.0, username = "olga")
    private SpendDto restaurantSpend;

    @WithSpend(category = PRODUCTS, amount = 2000.0, description = "Лента", username = "olga")
    private SpendDto productSpend;

    @BeforeEach
    void login() {
        landingPage.open()
                .clickLoginButton();
        loginPage.login("olga", "12345");
    }

    @Test
    void historyOfSpendingTableContainsSpending() {
        homePage.getSpendingTable()
                .checkContainsSpending(restaurantSpend, productSpend);
    }

}
