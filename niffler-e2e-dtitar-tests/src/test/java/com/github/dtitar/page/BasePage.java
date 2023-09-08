package com.github.dtitar.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.github.dtitar.config.Config;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage<T extends BasePage> {

    protected static final Config CFG = Config.getConfig();

    private final SelenideElement tostifyAlert = $("div .Toastify__toast-body");

    public abstract T waitForPageLoaded();

    @Step("Check that success message appears: {expectedText}")
    @SuppressWarnings("unchecked")
    public T checkToasterMessage(String expectedText) {
        tostifyAlert.should(Condition.visible)
                .should(Condition.text(expectedText));
        return (T) this;
    }
}
