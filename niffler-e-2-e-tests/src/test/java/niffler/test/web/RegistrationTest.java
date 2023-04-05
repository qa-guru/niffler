package niffler.test.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.User;
import niffler.model.UserJson;
import niffler.page.MainPage;
import niffler.page.WelcomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static niffler.jupiter.extension.CreateUserExtension.Selector.METHOD;
import static niffler.utils.DataUtils.generateRandomPassword;
import static niffler.utils.DataUtils.generateRandomUsername;
import static niffler.utils.Error.PASSWORDS_SHOULD_BE_EQUAL;

public class RegistrationTest extends BaseTest{

    @Test
    @AllureId("7")
    @DisplayName("WEB: Пользователь может успешно зарегистрироваться в сиситеме")
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
    @AllureId("8")
    @DisplayName("WEB: При регистрации возникает ошибка, если пользлватель с таким юзернеймом уже существует")
    @GenerateUser()
    void shouldNotRegisterUserWithExistingUsername(@User(selector = METHOD) UserJson existingUser) {
        String username = existingUser.getUsername();
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
    @AllureId("9")
    @DisplayName("WEB: При регистрации возникает ошибка, если введены разные пароль и подтверждение пароля")
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
