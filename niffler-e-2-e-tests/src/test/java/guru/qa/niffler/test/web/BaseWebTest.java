package guru.qa.niffler.test.web;

import com.codeborne.selenide.logevents.SelenideLogger;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.DBTest;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import io.qameta.allure.selenide.AllureSelenide;
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
