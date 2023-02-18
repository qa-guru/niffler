package niffler.test;

import niffler.jupiter.Spend;
import niffler.page.LoginPage;
import niffler.model.SpendModel;

import niffler.page.MainPage;
import org.junit.jupiter.api.Test;

import static niffler.model.Currency.EUR;

public class SpendsTableTest {

    @Spend(category = "Кафе", currency = EUR, amount = 5.5, description = "Матча латте", username = "Kate")
    SpendModel spend;

    LoginPage loginPage = new LoginPage();
    MainPage mainPage = new MainPage();

    @Test
    void spendsTableHistory() {
        loginPage.login("Kate", "pass");
        mainPage.checkSpendPresence(spend);
    }
}
