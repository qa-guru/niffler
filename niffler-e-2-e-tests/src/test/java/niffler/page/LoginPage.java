package niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage> {

    public static final String URL = CFG.authUrl() + "login";

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");

    @Step("Fill login page with credentials: username: {0}, password: {1}")
    public LoginPage fillLoginPage(String login, String password) {
        usernameInput.setValue(login);
        passwordInput.setValue(password);
        return this;
    }

    @Step("Submit login")
    public MainPage submit() {
        submitButton.click();
        return new MainPage();
    }

    @Override
    public LoginPage waitForPageLoaded() {
        usernameInput.should(visible);
        passwordInput.should(visible);
        return this;
    }
}
