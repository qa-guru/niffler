package niffler.test.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.User;
import niffler.model.UserJson;
import niffler.page.LoginPage;
import niffler.page.MainPage;
import niffler.page.WelcomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static niffler.jupiter.extension.CreateUserExtension.Selector.METHOD;
import static niffler.utils.Error.BAD_CREDENTIALS;

public class LoginTest extends BaseTest {

    @Test
    @AllureId("1")
    @DisplayName("WEB: Главная страница должна отображаться после логина новым юзером")
    @GenerateUser()
    void mainPageShouldBeDisplayedAfterSuccessLogin(@User(selector = METHOD) UserJson user) {
        Selenide.open(WelcomePage.URL, WelcomePage.class)
                .doLogin()
                .fillLoginPage(user.getUsername(), user.getPassword())
                .submit(new MainPage())
                .waitForPageLoaded();
    }

    @Test
    @AllureId("2")
    @DisplayName("WEB: При неверно введенных логине/пароле пользователь остается неавторизованным")
    @GenerateUser()
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials(@User(selector = METHOD) UserJson user) {
        LoginPage loginPage = Selenide.open(WelcomePage.URL, WelcomePage.class)
                .doLogin()
                .fillLoginPage(user.getUsername(), user.getPassword() + "BAD");

        loginPage.submit(loginPage)
                .checkError(BAD_CREDENTIALS.content);
    }
}
