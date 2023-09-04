package com.github.dtitar.web;

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

    private final String testLogin = "titar-test-10";
    private final String testPassword = "123123eE";


    @BeforeEach
    void doLogin() {
        System.setProperty("test.env", "local");
        open(WelcomePage.URL, WelcomePage.class)
                .doLogin()
                .fillLoginPage(testLogin, testPassword)
                .submit(MainPage.class);

    }

    @Test
    @Spend(username = testLogin,
            category = "Fishing",
            description = "Volga Fishing",
            amount = 20000,
            currency = CurrencyValues.RUB)
    void spendingShouldBeDeletedAfterDeleteAction(SpendJson createdSpend) {

    }
}
