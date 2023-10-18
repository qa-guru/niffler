package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.AllPeoplePageRustam;
import guru.qa.niffler.page.FriendsPageRustam;
import guru.qa.niffler.page.MainPageRustam;
import guru.qa.niffler.page.ProfilePageRustam;

public class HeaderRustam extends BaseComponentRustam<HeaderRustam> {

  private final SelenideElement mainPageBtn = $("a[href*='main']");
  private final SelenideElement friendsPageBtn = $("a[href*='friends']");
  private final SelenideElement allPeoplePageBtn = $("a[href*='people']");
  private final SelenideElement profilePageBtn = $("a[href*='profile']");

  public HeaderRustam() {
    super($(".header"));
  }

  @Override
  public HeaderRustam checkThatComponentDisplayed() {
    self.$(".header__title").shouldHave(text("Niffler. The coin keeper."));
    return null;
  }

  public MainPageRustam goToMainPage() {
    mainPageBtn.click();
    return new MainPageRustam();
  }

  public FriendsPageRustam goToFriendsPage() {
    friendsPageBtn.click();
    return new FriendsPageRustam();
  }

  public AllPeoplePageRustam goToAllPeoplePage() {
    allPeoplePageBtn.click();
    return new AllPeoplePageRustam();
  }

  public ProfilePageRustam goToProfilePage() {
    profilePageBtn.click();
    return new ProfilePageRustam();
  }

}
