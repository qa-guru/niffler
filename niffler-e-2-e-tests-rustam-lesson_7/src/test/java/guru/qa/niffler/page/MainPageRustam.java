package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.HeaderRustam;

public class MainPageRustam extends BasePageRustam<MainPageRustam> {

  public final static String URL = "http://127.0.0.1:3000/main";

  private final HeaderRustam headerRustam = new HeaderRustam();

  public HeaderRustam getHeader() {
    return headerRustam;
  }

  private final SelenideElement spendingBlock = $(".main-content__section-add-spending h2");
  private final SelenideElement logoutBtn = $("button.button-icon_type_logout");

  @Override
  public MainPageRustam checkThatPageLoaded() {
    spendingBlock.shouldHave(text("Add new spending"));
    return this;
  }

  public MainPageRustam doLogout() {
    logoutBtn.click();
    return this;
  }
}
