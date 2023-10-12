package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.AllPeoplePage;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;

public class Header extends BaseComponent<Header> {

  private final SelenideElement mainPageBtn = $("a[href*='main']");
  private final SelenideElement friendsPageBtn = $("a[href*='friends']");
  private final SelenideElement allPeoplePageBtn = $("a[href*='people']");
  private final SelenideElement profilePageBtn = $("a[href*='profile']");
  private final SelenideElement logoutBtn = $("button[data-tooltip-id='logout']");

  public Header() {
    super($(".header"));
  }

  @Override
  public Header checkThatComponentDisplayed() {
    self.$(".header__title").shouldHave(text("Niffler. The coin keeper."));
    return null;
  }

  public MainPage goToMainPage() {
    mainPageBtn.click();
    return new MainPage();
  }

  public FriendsPage goToFriendsPage() {
    friendsPageBtn.click();
    return new FriendsPage();
  }

  public AllPeoplePage gotToAllPeoplePage() {
    allPeoplePageBtn.click();
    return new AllPeoplePage();
  }

  public ProfilePage goToProfilePage() {
    profilePageBtn.click();
    return new ProfilePage();
  }

}
