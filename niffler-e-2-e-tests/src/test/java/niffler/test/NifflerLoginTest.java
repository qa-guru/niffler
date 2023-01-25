package niffler.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import niffler.jupiter.ScreenshotExtension;
import niffler.jupiter.User;
import niffler.jupiter.UsersExtension;
import niffler.model.UserModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.$;
import static niffler.jupiter.User.UserType.ADMIN;

@ExtendWith({ScreenshotExtension.class, UsersExtension.class})
public class NifflerLoginTest {

    @AllureId("1")
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin(@User(userType = ADMIN) UserModel user) {
        System.out.println("#### Test 1 " + user.toString());
        Allure.step("Check login", () -> {
            Selenide.open("http://127.0.0.1:3000/");
            $("a[href*='redirect']").click();
            $("input[name='username']").setValue("dima");
            $("input[name='password']").setValue("12345");
            $("button[type='submit']").click();
            $(".header__title").shouldBe(Condition.visible)
                    .shouldHave(Condition.text("Niffler. The coin keeper."));
        });
    }

    @AllureId("2")
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin0(@User(userType = ADMIN) UserModel user) {
        System.out.println("#### Test 2 " + user.toString());
        Allure.step("Check login", () -> {
            Selenide.open("http://127.0.0.1:3000/");
            $("a[href*='redirect']").click();
            $("input[name='username']").setValue("dima");
            $("input[name='password']").setValue("12345");
            $("button[type='submit']").click();
            $(".header__title").shouldBe(Condition.visible)
                    .shouldHave(Condition.text("Niffler. The coin keeper."));
        });
    }

    @AllureId("3")
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin1(@User UserModel user) {
        System.out.println("#### Test 3 " + user.toString());
        Allure.step("Check login", () -> {
            Selenide.open("http://127.0.0.1:3000/");
            $("a[href*='redirect']").click();
            $("input[name='username']").setValue("dima");
            $("input[name='password']").setValue("12345");
            $("button[type='submit']").click();
            $(".header__title").shouldBe(Condition.visible)
                    .shouldHave(Condition.text("Niffler. The coin keeper."));
        });
    }

    @AllureId("4")
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin2(@User UserModel user) {
        System.out.println("#### Test 4 " + user.toString());
        Allure.step("Check login", () -> {
            Selenide.open("http://127.0.0.1:3000/");
            $("a[href*='redirect']").click();
            $("input[name='username']").setValue("dima");
            $("input[name='password']").setValue("12345");
            $("button[type='submit']").click();
            $(".header__title").shouldBe(Condition.visible)
                    .shouldHave(Condition.text("Niffler. The coin keeper."));
        });
    }

    @AllureId("5")
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin3(@User UserModel user) {
        System.out.println("#### Test 5 " + user.toString());
        Allure.step("Check login", () -> {
            Selenide.open("http://127.0.0.1:3000/");
            $("a[href*='redirect']").click();
            $("input[name='username']").setValue("dima");
            $("input[name='password']").setValue("12345");
            $("button[type='submit']").click();
            $(".header__title").shouldBe(Condition.visible)
                    .shouldHave(Condition.text("Niffler. The coin keeper."));
        });
    }


}
