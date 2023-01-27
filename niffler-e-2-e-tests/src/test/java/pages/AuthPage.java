package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class AuthPage {
    private final SelenideElement
            usernameField = $("input[name='username']");
    private final SelenideElement
            passwordField = $("input[name='password']");
    private final SelenideElement
            submitButton = $("button[type='submit']");


    @Step("Открыть страницу авторизации")
    public void open() {
        Selenide.open("http://auth-server:9000/login");
    }

    @Step("Ввести юзер креды и авторизоваться")
    public void authorizeWithCredentials(String login, String pass) {
        usernameField.setValue(login);
        passwordField.setValue(pass);
        submitButton.click();
    }
}
