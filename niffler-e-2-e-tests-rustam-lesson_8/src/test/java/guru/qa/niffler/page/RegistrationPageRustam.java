package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import org.openqa.selenium.By;

public class RegistrationPageRustam extends BasePageRustam<RegistrationPageRustam> {

  // может быть статическим так как это константные типы
  private static final By ByHeader = By.cssSelector(".form__paragraph");
  private static final String StringHeader = ".form__paragraph";
  public static final String REGISTRATION_URL = Config.getInstance().getAuthUrl() + "/register";

  // не может быть статическим, т.к. мутабл объект
  private final SelenideElement header = $(".form__paragraph");
  private final SelenideElement usernameInput = $("#username");
  private final SelenideElement passwordInput = $("#password");
  private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
  private final SelenideElement signUpBtn = $("button[type='submit']");

  @Override
  public RegistrationPageRustam checkThatPageLoaded() {
    header.shouldHave(text("Registration form"));
    return this;
  }

  public RegistrationPageRustam fillRegistrationForm(String username, String password, String passwordSubmit) {
    usernameInput.val(username);
    passwordInput.val(password);
    passwordSubmitInput.val(passwordSubmit);
    signUpBtn.click();
    return this;
  }

  public RegistrationPageRustam checkErrorMessage(String expectedMessage) {
    $(".form__error").shouldHave(text(expectedMessage));
    return this;
  }

}
