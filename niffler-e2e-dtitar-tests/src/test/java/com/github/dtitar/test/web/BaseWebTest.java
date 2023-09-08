package com.github.dtitar.test.web;

import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.dtitar.config.Config;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseWebTest {
    protected static final Config CFG = Config.getConfig();

    @BeforeEach
    void setup() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(false)
                .savePageSource(false)
        );
    }
}
