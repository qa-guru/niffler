package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.jupiter.annotation.ClasspathUser;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginTest extends BaseWebTest {

    private static ObjectMapper om = new ObjectMapper();
    private ClassLoader cl = LoginTest.class.getClassLoader();

    @AllureId("104")
    @Test
    void loginTest(UserJson user) {
        Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(user.getPassword());
        $("button[type='submit']").click();

        $("a[href*='friends']").click();
        $(".header").should(visible).shouldHave(text("Niffler. The coin keeper."));
    }

    @AllureId("105")
    @ValueSource(strings = {
          "testdata/rustam.json",
          "testdata/emma.json"
    })
    @ParameterizedTest
    void loginParametrizedTest(String pathToUser) throws IOException {
        UserJson user = om.readValue(cl.getResourceAsStream(pathToUser), UserJson.class);

        Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(user.getPassword());
        $("button[type='submit']").click();

        $("a[href*='friends']").click();
        $(".header").should(visible).shouldHave(text("Niffler. The coin keeper."));
    }

    @AllureId("106")
    @ValueSource(strings = {
          "testdata/rustam.json",
          "testdata/emma.json"
    })
    @ParameterizedTest
    void loginParametrizedAnnotatedTest(@ClasspathUser UserJson user) throws IOException {

        Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(user.getPassword());
        $("button[type='submit']").click();

        $("a[href*='friends']").click();
        $(".header").should(visible).shouldHave(text("Niffler. The coin keeper."));
    }


}
