package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.WelcomePage;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.annotation.User.Selector.METHOD;
import static guru.qa.niffler.utils.ErrorMessage.BAD_CREDENTIALS;

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
                .fillLoginPage(user.username(), user.testData().password())
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
                .fillLoginPage(user.username(), user.testData().password() + "BAD");

        loginPage.submit(loginPage)
                .checkError(BAD_CREDENTIALS.content);
    }
}
