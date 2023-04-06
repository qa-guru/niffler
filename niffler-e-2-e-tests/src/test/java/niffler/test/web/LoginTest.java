package niffler.test.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.User;
import niffler.model.rest.UserJson;
import niffler.page.LoginPage;
import niffler.page.MainPage;
import niffler.page.WelcomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static niffler.jupiter.extension.CreateUserExtension.Selector.METHOD;
import static niffler.utils.Error.BAD_CREDENTIALS;

@Epic("[WEB][niffler-frontend]: Авторизация")
@DisplayName("[WEB][niffler-frontend]: Авторизация")
public class LoginTest extends BaseWebTest {

    @Test
    @AllureId("500001")
    @DisplayName("WEB: Главная страница должна отображаться после логина новым юзером")
    @Tag("WEB")
    @GenerateUser()
    void mainPageShouldBeDisplayedAfterSuccessLogin(@User(selector = METHOD) UserJson user) {
        Selenide.open(WelcomePage.URL, WelcomePage.class)
                .doLogin()
                .fillLoginPage(user.getUsername(), user.getPassword())
                .submit(new MainPage())
                .waitForPageLoaded();
    }

    @Test
    @AllureId("500002")
    @DisplayName("WEB: При неверно введенных логине/пароле пользователь остается неавторизованным")
    @Tag("WEB")
    @GenerateUser()
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials(@User(selector = METHOD) UserJson user) {
        LoginPage loginPage = Selenide.open(WelcomePage.URL, WelcomePage.class)
                .doLogin()
                .fillLoginPage(user.getUsername(), user.getPassword() + "BAD");

        loginPage.submit(loginPage)
                .checkError(BAD_CREDENTIALS.content);
    }
}
