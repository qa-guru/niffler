package niffler.page;

import config.App;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class LoginPage {

    public void login(String username, String password) {
        open(App.FRONTEND_URL);
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(username);
        $("input[name='password']").setValue(password);
        $("button[type='submit']").click();
        $(".header__title").shouldBe(visible)
                .shouldHave(text("Niffler. The coin keeper."));
    }
}
