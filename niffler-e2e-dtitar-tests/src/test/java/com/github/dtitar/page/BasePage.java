package com.github.dtitar.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.github.dtitar.config.AppConfig;
import io.qameta.allure.Step;
import org.aeonbits.owner.ConfigCache;

import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage<T extends BasePage> {

    protected static final AppConfig CFG = ConfigCache.getOrCreate(AppConfig.class);

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
