package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;

public class LoginPageRustam extends BasePageRustam<LoginPageRustam> {

  public static final String LOGIN_URL = Config.getInstance().getAuthUrl() + "/login";

  private final SelenideElement header = $(".form__paragraph");
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement signUpBtn = $("button[type='submit']");
  private final SelenideElement registerLink = $("a[href*='register']");

  @Override
  public LoginPageRustam checkThatPageLoaded() {
    header.shouldHave(text("Please sign in"));
    return this;
  }

  public LoginPageRustam fillSignInForm(String username, String password) {
    usernameInput.val(username);
    passwordInput.val(password);
    signUpBtn.click();
    return this;
  }

  public LoginPageRustam checkErrorMessage(String expectedMessage) {
    $(".form__error").shouldHave(text(expectedMessage));
    return this;
  }

  public RegistrationPageRustam goToRegisterPage() {
    registerLink.click();
    return new RegistrationPageRustam();
  }
}
