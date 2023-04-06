package niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class Select extends BaseComponent<Select> {

    public Select(SelenideElement self) {
        super(self);
    }

    private final SelenideElement input = self.$("input");

    public void setValue(String value) {
        input.setValue(value);
        $$("div[id^='react-select']").find(exactText(value)).click();
    }

    public void checkSelectValueIsEqualTo(String value) {
        self.shouldHave(text(value));
    }
}
