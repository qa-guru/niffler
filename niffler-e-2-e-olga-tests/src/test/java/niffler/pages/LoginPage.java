package niffler.pages;

import io.qameta.allure.Step;
import niffler.config.app.AppProperties;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage extends BasePage {

    @Step("Login")
    public void login(String username, String password) {
        open(AppProperties.FRONTEND_APP_URL);
        $("a[href*='redirect']").click();
        $("[name=username]").setValue(username);
        $("[name=password]").setValue(password);
        $("button[type='submit']").click();
        $(".header__title").shouldBe(visible)
                .shouldHave(text("Niffler. The coin keeper."));
    }
}
