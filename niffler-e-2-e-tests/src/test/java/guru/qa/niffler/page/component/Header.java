package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.page.ProfilePage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class Header extends BaseComponent<Header> {

  public Header() {
    super($("#root header"));
  }

  private final SelenideElement mainPageLink = self.$("a[href*='/main']");
  private final SelenideElement addSpendingBtn = self.$("a[href*='/spending']");
  private final SelenideElement menuBtn = self.$("button");
  private final SelenideElement menu = self.$("ul[role='menu']");
  private final ElementsCollection menuItems = menu.$$("li");

  @Step("Open Friends page")
  @Nonnull
  public FriendsPage toFriendsPage() {
    menuBtn.click();
    menuItems.find(text("Friends")).click();
    return new FriendsPage();
  }

  @Step("Open All Peoples page")
  @Nonnull
  public PeoplePage toAllPeoplesPage() {
    menuBtn.click();
    menuItems.find(text("All People")).click();
    return new PeoplePage();
  }

  @Step("Open Profile page")
  @Nonnull
  public ProfilePage toProfilePage() {
    menuBtn.click();
    menuItems.find(text("Profile")).click();
    return new ProfilePage();
  }

  @Step("Sign out")
  @Nonnull
  public LoginPage signOut() {
    menuBtn.click();
    menuItems.find(text("Sign out")).click();
    return new LoginPage();
  }

  @Step("Add new spending")
  @Nonnull
  public EditSpendingPage addSpendingPage() {
    addSpendingBtn.click();
    return new EditSpendingPage();
  }

  @Step("Go to main page")
  @Nonnull
  public MainPage toMainPage() {
    mainPageLink.click();
    return new MainPage();
  }
}
