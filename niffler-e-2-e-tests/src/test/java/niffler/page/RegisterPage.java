package niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage extends BasePage<RegisterPage> {

    public static final String URL = CFG.authUrl() + "register";

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement proceedLoginLink = $("a[href*='redirect']");

    @Step("Fill register page with credentials: username: {0}, password: {1}")
    public RegisterPage fillLoginPage(String login, String password) {
        usernameInput.setValue(login);
        passwordInput.setValue(password);
        passwordSubmitInput.setValue(password);
        return this;
    }

    @Step("Submit register")
    public LoginPage submit() {
        submitButton.click();
        proceedLoginLink.click();
        return new LoginPage();
    }

    @Override
    public RegisterPage waitForPageLoaded() {
        usernameInput.should(visible);
        passwordInput.should(visible);
        passwordSubmitInput.should(visible);
        return this;
    }
}
