package niffler.page;

import io.qameta.allure.Step;
import niffler.config.Config;

public abstract class BasePage<T extends BasePage> {

    protected static final Config CFG = Config.getConfig();

    @Step("Check that page is loaded")
    public abstract T waitForPageLoaded();
}
