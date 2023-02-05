package niffler.pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage {

    @Step("Login")
    public void login(String username, String password) {
        $("[name=username]").setValue(username);
        $("[name=password]").setValue(password);
        $("[type=submit]").click();
    }

}
