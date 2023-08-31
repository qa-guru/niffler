package com.github.dtitar.web;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.dtitar.config.AppConfig;
import com.github.dtitar.config.DriverConfig;
import io.qameta.allure.selenide.AllureSelenide;
import org.aeonbits.owner.ConfigCache;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseWebTest {
    protected static final AppConfig APP_CONFIG = ConfigCache.getOrCreate(AppConfig.class);
    protected static final DriverConfig DRIVER_CONFIG = ConfigCache.getOrCreate(DriverConfig.class);

    @BeforeAll
    static void setupAll() {
        if ("docker".equals(System.getProperty("test.env"))) {
            setDockerDriverConfig();
        } else if ("local".equals(System.getProperty("test.env"))) {
            setLocalDriverConfig();
        } else {
            throw new IllegalStateException();
        }
    }

    @BeforeEach
    void setup() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(false)
                .savePageSource(false)
        );
    }

    private static void setLocalDriverConfig() {
        Configuration.browserSize = DRIVER_CONFIG.browserSize();
    }

    private static void setDockerDriverConfig() {
        Configuration.browserSize = DRIVER_CONFIG.browserSize();
        Configuration.remote = DRIVER_CONFIG.remoteUrl();
        Configuration.timeout = 10000;
    }

}
