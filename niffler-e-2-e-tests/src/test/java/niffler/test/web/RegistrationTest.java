package niffler.test.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.User;
import niffler.model.rest.UserJson;
import niffler.page.MainPage;
import niffler.page.WelcomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static niffler.jupiter.extension.CreateUserExtension.Selector.METHOD;
import static niffler.utils.DataUtils.generateRandomPassword;
import static niffler.utils.DataUtils.generateRandomUsername;
import static niffler.utils.Error.PASSWORDS_SHOULD_BE_EQUAL;

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
