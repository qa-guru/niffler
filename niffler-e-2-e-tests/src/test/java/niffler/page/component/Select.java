package niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class Select extends BaseComponent<Select> {
    public Select(SelenideElement self) {
        super(self);
    }

    public void setValue(String value) {
        self.$("div[class*='IndicatorsContainer']").scrollIntoView(false).click();
        $$("div[id^='react-select']").find(exactText(value)).click();
    }

    public void checkSelectValueIsEqualTo(String value) {
        self.shouldHave(text(value));
    }
}
