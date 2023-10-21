package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class Select extends BaseComponent<Select> {

    public Select(@Nonnull SelenideElement self) {
        super(self);
    }

    private final SelenideElement input = self.$("input");

    public void setValue(@Nonnull String value) {
        input.setValue(value);
        $$("div[id^='react-select']").find(exactText(value)).click();
    }

    public void checkSelectValueIsEqualTo(@Nonnull String value) {
        self.shouldHave(text(value));
    }
}
