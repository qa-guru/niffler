package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.HeaderRustam;

public class ProfilePageRustam extends BasePageRustam<ProfilePageRustam> {

  private final HeaderRustam headerRustam = new HeaderRustam();

  public HeaderRustam getHeader() {
    return headerRustam;
  }

  private final SelenideElement avatarBlock = $("div .main-content__section-avatar");

  @Override
  public ProfilePageRustam checkThatPageLoaded() {
    avatarBlock.should(visible);
    return this;
  }
}
