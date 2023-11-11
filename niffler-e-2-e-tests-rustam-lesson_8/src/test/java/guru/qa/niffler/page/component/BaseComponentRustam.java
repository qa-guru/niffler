package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

public abstract class BaseComponentRustam<T extends BaseComponentRustam> {

  protected final SelenideElement self;

  protected BaseComponentRustam(SelenideElement self) {
    this.self = self;
  }

  public abstract T checkThatComponentDisplayed();
}
