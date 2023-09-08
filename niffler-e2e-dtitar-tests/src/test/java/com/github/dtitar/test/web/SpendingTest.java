package com.github.dtitar.test.web;

import com.github.dtitar.jupiter.annotation.Category;
import com.github.dtitar.jupiter.annotation.DeleteSpends;
import com.github.dtitar.jupiter.annotation.Spend;
import com.github.dtitar.model.rest.CurrencyValues;
import com.github.dtitar.model.rest.SpendJson;
import com.github.dtitar.page.MainPage;
import com.github.dtitar.page.WelcomePage;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

@Epic("[WEB][niffler-frontend]: Траты")
@DisplayName("[WEB][niffler-frontend]: Траты")
public class SpendingTest extends BaseWebTest {

    //todo: these data are only for test purposes. Replace to @GenerateUser
    private final String testLogin = "titar-test-1";
    private final String testPassword = "123123ee";
    private MainPage mainPage;

    @BeforeEach
    void doLogin() {
        mainPage = open(WelcomePage.URL, WelcomePage.class)
                .doLogin()
                .fillLoginPage(testLogin, testPassword)
                .submit(MainPage.class);
    }

    @Test
    @DeleteSpends(username = testLogin)
    @Category(
            username = testLogin,
            description = "Hunting")
    @Spend(username = testLogin,
            category = "Hunting",
            description = "Rocketlauncher",
            amount = 3500,
            currency = CurrencyValues.RUB)
    void spendingShouldBeDisplayedAtTheSpendingTable(SpendJson createdSpend) {
        mainPage
                .getSpendingTable()
                .checkTableContains(createdSpend);
    }

    @Test
    @DeleteSpends(username = testLogin)
    @Category(
            username = testLogin,
            description = "Hunting")
    @Spend(username = testLogin,
            category = "Hunting",
            description = "Rocketlauncher",
            amount = 3800,
            currency = CurrencyValues.RUB)
    void spendingShouldBeDeletedAfterDeleteAction(SpendJson createdSpend) {
        mainPage.getSpendingTable()
                .deleteSpending(createdSpend.getDescription())
                .checkSpendingTableIsEmpty();
    }
}
