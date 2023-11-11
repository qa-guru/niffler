package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.condition.photo.PhotoCondition;
import guru.qa.niffler.db.entity.userdata.CurrencyValues;
import guru.qa.niffler.page.component.HeaderRustam;
import io.qameta.allure.Step;

public class ProfilePageRustam extends BasePageRustam<ProfilePageRustam> {

  public static final String URL = CFG.getFrontUrl() + "profile";

  private final SelenideElement nameInput = $("input[name='firstname']");
  private final SelenideElement surnameInput = $("input[name='surname']");
  private final SelenideElement categoryInput = $("input[name='category']");
  private final SelenideElement currencySelect = $("div .select-wrapper");
  private final SelenideElement submitButton = $(byText("Submit"));
  private final SelenideElement createCategoryButton = $(byText("Create"));

  @Step("Open profile page")
  public ProfilePageRustam open() {
    Selenide.open(URL);
    return this;
  }

  @Step("Fill profile page with rate: name: {0}, surname: {1}, currency: {2}")
  public ProfilePageRustam fillProfile(String name, String surname, CurrencyValues currency) {
    setName(name);
    setSurname(surname);
    setCurrency(currency);
    submitProfile();
    return this;
  }

  @Step("Set name: {0}")
  public ProfilePageRustam setName(String name) {
    nameInput.setValue(name);
    return this;
  }

  @Step("Set surname: {0}")
  public ProfilePageRustam setSurname(String surname) {
    surnameInput.setValue(surname);
    return this;
  }

  @Step("Set currency: {0}")
  public ProfilePageRustam setCurrency(CurrencyValues currency) {
    currencySelect.click();
    $$("div[id^='react-select']").find(text(currency.name())).click();
    return this;
  }

  @Step("Set photo by path: {fileClasspath}")
  public ProfilePageRustam setPhoto(String fileClasspath) {
    $(".profile__avatar").click();
    $(".edit-avatar__input[type=file]").uploadFromClasspath(fileClasspath);
    return this;
  }

  @Step("Check name: {0}")
  public ProfilePageRustam checkName(String name) {
    nameInput.shouldHave(value(name));
    return this;
  }

  @Step("Check surname: {0}")
  public ProfilePageRustam checkSurname(String surname) {
    surnameInput.shouldHave(value(surname));
    return this;
  }

  @Step("Check currency: {0}")
  public ProfilePageRustam checkCurrency(CurrencyValues currency) {
    currencySelect.shouldHave(text(currency.name()));
    return this;
  }

  @Step("Check photo by path: {expectedPhotoClasspath}")
  public ProfilePageRustam checkPhoto(String expectedPhotoClasspath) {
    $(".profile__avatar").shouldHave(PhotoCondition.photo(expectedPhotoClasspath));
    return this;
  }

  @Step("Save profile")
  public ProfilePageRustam submitProfile() {
    submitButton.click();
    return this;
  }

  @Step("Check that page is loaded")
  @Override
  public ProfilePageRustam checkThatPageLoaded() {
    nameInput.should(visible);
    return this;
  }
}
