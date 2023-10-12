package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;

public class LoginPage extends BasePage<LoginPage> {

  public static final String LOGIN_URL = Config.getInstance().getFrontUrl() + "/login";

  private final SelenideElement header = $(".form__paragraph");
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement signUpBtn = $("button[type='submit']");
  private final SelenideElement loginBtn = $("a[href*='redirect']");

  @Override
  public LoginPage checkThatPageLoaded() {
    loginBtn.click();
    header.shouldHave(text("Please sign in"));
    return this;
  }

  public LoginPage fillSignInForm(String username, String password) {
    usernameInput.val(username);
    passwordInput.val(password);
    signUpBtn.click();
    return this;
  }
}
