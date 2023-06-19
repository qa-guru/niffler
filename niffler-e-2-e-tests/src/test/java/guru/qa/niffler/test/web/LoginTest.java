package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateUserInAuthDb;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;


public class LoginTest extends BaseWebTest {

    private static final String TEST_USERNAME = "dima";
    private static final String TEST_PWD = "12345";

    @GenerateUserInAuthDb(username = TEST_USERNAME, password = TEST_PWD)
    @AllureId("104")
    @Test
    void loginTest(UserJson user) throws IOException {
        Allure.step("open page", () -> Selenide.open(CFG.getFrontUrl() + "/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(user.getPassword());
        $("button[type='submit']").click();

        $("a[href*='friends']").click();
        $(".header").should(visible).shouldHave(text("Niffler. The coin keeper."));
    }

}
