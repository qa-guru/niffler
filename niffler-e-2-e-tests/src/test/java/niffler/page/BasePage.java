package niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import niffler.config.Config;

import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage<T extends BasePage> {

    protected static final Config CFG = Config.getConfig();

    private final SelenideElement tostifyAlert = $("div .Toastify__toast-body");

    public abstract T waitForPageLoaded();

    @SuppressWarnings("unchecked")
    public T checkToasterMessage(String expectedText) {
        tostifyAlert.should(Condition.visible).should(Condition.text(expectedText));
        return (T) this;
    }
}
