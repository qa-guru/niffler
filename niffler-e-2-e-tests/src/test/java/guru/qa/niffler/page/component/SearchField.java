package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Selenide.$;

public class SearchField extends BaseComponent<SearchField> {
  public SearchField(@Nonnull SelenideElement self) {
    super(self);
  }

  public SearchField() {
    super($("input[aria-label='search']"));
  }

  private final SelenideElement clearSearchInputBtn = $("#input-clear");

  public SearchField search(String query) {
    clearIfNotEmpty();
    self.setValue(query).pressEnter();
    return this;
  }

  public SearchField clearIfNotEmpty() {
    if (self.is(not(empty))) {
      clearSearchInputBtn.click();
      self.should(empty);
    }
    return this;
  }
}
