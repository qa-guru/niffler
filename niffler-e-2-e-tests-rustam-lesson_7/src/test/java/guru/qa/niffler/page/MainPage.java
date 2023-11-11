package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;
import guru.qa.niffler.page.component.Footer;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class MainPage extends BasePageRustam<MainPage> {

  public static final String URL = CFG.getFrontUrl() + "main";

  protected final Header header = new Header();
  protected final Footer footer = new Footer();
  protected final SpendingTable spendingTable = new SpendingTable();

  public Header getHeader() {
    return header;
  }

  public Footer getFooter() {
    return footer;
  }

  public SpendingTable getSpendingTable() {
    return spendingTable;
  }

  @Step("Open main page")
  public MainPage open() {
    Selenide.open(URL);
    return this;
  }

  @Step("Check that page is loaded")
  @Override
  public MainPage checkThatPageLoaded() {
    header.getSelf().should(visible).shouldHave(text("Niffler. The coin keeper."));
    footer.getSelf().should(visible).shouldHave(text("Study project for QA Automation Advanced. 2023"));
    spendingTable.getSelf().should(visible).shouldHave(text("History of spendings"));
    return this;
  }
}
