package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.WelcomePage;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.annotation.User.Selector.METHOD;
import static guru.qa.niffler.utils.DataUtils.generateRandomPassword;
import static guru.qa.niffler.utils.DataUtils.generateRandomUsername;
import static guru.qa.niffler.utils.ErrorMessage.PASSWORDS_SHOULD_BE_EQUAL;

@Epic("[WEB][niffler-frontend]: Регистрация")
@DisplayName("[WEB][niffler-frontend]: Регистрация")
public class RegistrationTest extends BaseWebTest {

    @Test
    @AllureId("500008")
    @DisplayName("WEB: Пользователь может успешно зарегистрироваться в сиситеме")
    @Tag("WEB")
    void shouldRegisterNewUser() {
        String newUsername = generateRandomUsername();
        String password = generateRandomPassword();
        Selenide.open(WelcomePage.URL, WelcomePage.class)
                .waitForPageLoaded()
                .doRegister()
                .waitForPageLoaded()
                .setUsername(newUsername)
                .setPassword(password)
                .setPasswordSubmit(password)
                .successSubmit()
                .fillLoginPage(newUsername, password)
                .submit(new MainPage())
                .waitForPageLoaded();
    }

    @Test
    @AllureId("500009")
    @DisplayName("WEB: При регистрации возникает ошибка, если пользлватель с таким юзернеймом уже существует")
    @Tag("WEB")
    @GenerateUser()
    void shouldNotRegisterUserWithExistingUsername(@User(selector = METHOD) UserJson existingUser) {
        String username = existingUser.username();
        String password = generateRandomPassword();
        Selenide.open(WelcomePage.URL, WelcomePage.class)
                .waitForPageLoaded()
                .doRegister()
                .waitForPageLoaded()
                .setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(password)
                .errorSubmit()
                .checkErrorMessage("Username `" + username + "` already exists");
    }

    @Test
    @AllureId("500010")
    @DisplayName("WEB: При регистрации возникает ошибка, если введены разные пароль и подтверждение пароля")
    @Tag("WEB")
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        String username = generateRandomUsername();
        String password = generateRandomPassword();
        String submitPassword = generateRandomPassword();

        Selenide.open(WelcomePage.URL, WelcomePage.class)
                .waitForPageLoaded()
                .doRegister()
                .waitForPageLoaded()
                .setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(submitPassword)
                .errorSubmit()
                .checkErrorMessage(PASSWORDS_SHOULD_BE_EQUAL.content);
    }
}
