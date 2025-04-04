package guru.qa.niffler.page;

import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.StatComponent;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {

  public static final String URL = CFG.frontUrl() + "main";

  protected final Header header = new Header();
  protected final SpendingTable spendingTable = new SpendingTable();
  protected final StatComponent statComponent = new StatComponent();

  public Header getHeader() {
    return header;
  }

  @Nonnull
  public StatComponent getStatComponent() {
    return statComponent;
  }

  public SpendingTable getSpendingTable() {
    spendingTable.getSelf().scrollIntoView(true);
    return spendingTable;
  }

  @Step("Check that page is loaded")
  @Override
  public MainPage waitForPageLoaded() {
    header.getSelf().should(visible).shouldHave(text("Niffler"));
    spendingTable.getSelf().should(visible).shouldHave(text("History of Spendings"));
    statComponent.checkThatChartDisplayed();
    return this;
  }
}
