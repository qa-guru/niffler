package niffler.test;

import niffler.config.Config;
import niffler.jupiter.JpaExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(JpaExtension.class)
public abstract class BaseTest {

    protected static final Config CFG = Config.getConfig();
}
