package niffler.test.web;

import niffler.config.Config;
import niffler.jupiter.extension.JpaExtension;
import niffler.jupiter.extension.ScreenshotOnFailExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({JpaExtension.class, ScreenshotOnFailExtension.class})
public abstract class BaseTest {

    protected static final Config CFG = Config.getConfig();
}
