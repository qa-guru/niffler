package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;

public class RegistrationPage extends BasePage<RegistrationPage> {

  public static final String URL = Config.getConfig().getAuthUrl() + "/register";

  private final SelenideElement header = $(".form__paragraph");
  private final SelenideElement usernameInput = $("#username");
  private final SelenideElement passwordInput = $("#password");
  private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
  private final SelenideElement signUpBtn = $("button[type='submit']");
  private final SelenideElement formError = $(".form__error");

  @Override
  public RegistrationPage checkThatPageLoaded() {
    header.shouldHave(text("Registration form"));
    return this;
  }

  public RegistrationPage fillRegistrationForm(String username, String password, String passwordSubmit) {
    usernameInput.val(username);
    passwordInput.val(password);
    passwordSubmitInput.val(passwordSubmit);
    signUpBtn.click();
    return this;
  }

  public RegistrationPage checkErrorMessage(String expectedMessage) {
    formError.shouldHave(text(expectedMessage));
    return this;
  }
}
