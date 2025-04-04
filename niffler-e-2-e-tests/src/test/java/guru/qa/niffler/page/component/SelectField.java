package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class SelectField extends BaseComponent<SelectField> {

  public SelectField(SelenideElement self) {
    super(self);
  }

  private final SelenideElement input = self.$("input");

  public void setValue(String value) {
    self.click();
    $$("li[role='option']").find(text(value)).click();
  }

  public void checkSelectValueIsEqualTo(String value) {
    self.shouldHave(text(value));
  }
}
