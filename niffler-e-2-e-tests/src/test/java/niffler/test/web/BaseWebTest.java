package niffler.test.web;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import niffler.config.Config;
import niffler.jupiter.annotation.meta.DBTest;
import niffler.jupiter.annotation.meta.WebTest;
import org.junit.jupiter.api.BeforeEach;

@WebTest
@DBTest
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
