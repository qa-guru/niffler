package niffler.test.web;

import io.qameta.allure.junit5.AllureJunit5;
import niffler.config.Config;
import niffler.jupiter.extension.BrowserExtension;
import niffler.jupiter.extension.JpaExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({JpaExtension.class, BrowserExtension.class, AllureJunit5.class})
public abstract class BaseTest {

    protected static final Config CFG = Config.getConfig();
}
