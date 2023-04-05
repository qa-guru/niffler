package niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage extends BasePage<RegisterPage> {

    public static final String URL = CFG.authUrl() + "register";

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement proceedLoginLink = $("a[href*='redirect']");
    private final SelenideElement errorContainer = $(".form__error");

    @Step("Fill register page with credentials: username: {0}, password: {1}, submit password: {2}")
    public RegisterPage fillRegisterPage(String login, String password, String passwordSubmit) {
        setUsername(login);
        setPassword(password);
        setPasswordSubmit(passwordSubmit);
        return this;
    }

    @Step("Set username: {0}")
    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Step("Set password: {0}")
    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Confirm password: {0}")
    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmitInput.setValue(password);
        return this;
    }

    @Step("Submit register")
    public LoginPage successSubmit() {
        submitButton.click();
        proceedLoginLink.click();
        return new LoginPage();
    }

    @Step("Submit register")
    public RegisterPage errorSubmit() {
        submitButton.click();
        return this;
    }

    @Step("Check that page is loaded")
    @Override
    public RegisterPage waitForPageLoaded() {
        usernameInput.should(visible);
        passwordInput.should(visible);
        passwordSubmitInput.should(visible);
        return this;
    }

    public RegisterPage checkErrorMessage(String errorMessage) {
        errorContainer.shouldHave(text(errorMessage));
        return this;
    }
}
