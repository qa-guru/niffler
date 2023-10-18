package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.back;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;

public class WelcomePage extends BasePage<WelcomePage> {

  public static final String WELCOME_PAGE_URL = Config.getInstance().getFrontUrl() + "/login";

  private final SelenideElement header = $(".main__header");
  private final SelenideElement loginBtn = $("a[href*='redirect']");
  private final SelenideElement registerBtn = $("a[href*='register']");
  private final LoginPage loginPage = new LoginPage();
  private final RegistrationPage registrationPage = new RegistrationPage();

  @Override
  public WelcomePage checkThatPageLoaded() {
    header.shouldHave(text("Welcome to magic journey with Niffler. The coin keeper"));
    loginBtn.should(visible);
    registerBtn.should(visible);
    return this;
  }

  public WelcomePage checkWelcomeFormWorks() {
    checkThatPageLoaded();
    loginBtn.click();
    loginPage.checkThatPageLoaded();
    back();
    registerBtn.click();
    registrationPage.checkThatPageLoaded();
    back();
    loginPage.checkThatPageLoaded();
    return this;
  }

  public LoginPage doLogin() {
    loginBtn.click();
    return new LoginPage();
  }

  public RegistrationPage doRegister() {
    registerBtn.click();
    return new RegistrationPage();
  }
}
