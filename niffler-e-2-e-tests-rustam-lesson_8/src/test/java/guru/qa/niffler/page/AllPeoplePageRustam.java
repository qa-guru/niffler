package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.HeaderRustam;

public class AllPeoplePageRustam extends BasePageRustam<AllPeoplePageRustam> {

  private final HeaderRustam headerRustam = new HeaderRustam();

  public HeaderRustam getHeader() {
    return headerRustam;
  }

  private final SelenideElement peopleTable = $("div .people-content");

  @Override
  public AllPeoplePageRustam checkThatPageLoaded() {
    peopleTable.should(visible);
    return this;
  }
}
