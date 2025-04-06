package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static guru.qa.niffler.jupiter.annotation.User.Selector.METHOD;
import static guru.qa.niffler.utils.DataUtils.randomPassword;
import static guru.qa.niffler.utils.DataUtils.randomUsername;
import static guru.qa.niffler.utils.ErrorMessage.PASSWORDS_SHOULD_BE_EQUAL;
import static guru.qa.niffler.utils.ErrorMessage.PASSWORD_SHOULD_NOT_CONTAINS_WHITESPACES;
import static guru.qa.niffler.utils.ErrorMessage.USERNAME_SHOULD_NOT_CONTAINS_WHITESPACES;

@Epic(" [WEB][niffler-ng-client]: Регистрация")
@DisplayName(" [WEB][niffler-ng-client]: Регистрация")
public class RegistrationTest extends BaseWebTest {

  @Test
  @AllureId("500008")
  @DisplayName("WEB: Пользователь может успешно зарегистрироваться в сиситеме")
  @Tag("WEB")
  void shouldRegisterNewUser() {
    String newUsername = randomUsername();
    String password = randomPassword();
    Selenide.open(LoginPage.URL, LoginPage.class)
        .doRegister()
        .fillRegisterPage(newUsername, password, password)
        .successSubmit()
        .fillLoginPage(newUsername, password)
        .submit(new MainPage())
        .waitForPageLoaded();
  }

  @Test
  @AllureId("500009")
  @DisplayName("WEB: При регистрации возникает ошибка, если пользлватель с таким юзернеймом уже существует")
  @Tag("WEB")
  @GenerateUser
  void shouldNotRegisterUserWithExistingUsername(@User(selector = METHOD) UserJson existingUser) {
    String username = existingUser.username();
    String password = randomPassword();
    Selenide.open(LoginPage.URL, LoginPage.class)
        .doRegister()
        .fillRegisterPage(username, password, password)
        .errorSubmit()
        .checkAlertMessage("Username `" + username + "` already exists");
  }

  @Test
  @AllureId("500010")
  @DisplayName("WEB: При регистрации возникает ошибка, если введены разные пароль и подтверждение пароля")
  @Tag("WEB")
  void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
    String username = randomUsername();
    String password = randomPassword();
    String submitPassword = randomPassword();

    Selenide.open(LoginPage.URL, LoginPage.class)
        .doRegister()
        .fillRegisterPage(username, password, submitPassword)
        .errorSubmit()
        .checkAlertMessage(PASSWORDS_SHOULD_BE_EQUAL.content);
  }

  static Stream<Arguments> shouldShowErrorIfUsernameContainsWhiteSpace() {
    return Stream.of(
        Arguments.of(randomUsername() + " ", randomPassword(), USERNAME_SHOULD_NOT_CONTAINS_WHITESPACES.content),
        Arguments.of(randomUsername(), randomPassword() + " ", PASSWORD_SHOULD_NOT_CONTAINS_WHITESPACES.content)
    );
  }

  @MethodSource
  @ParameterizedTest(name = "При регистрации возникает ошибка {2}, если введено имя пользователя или пароль, содержащее пробелы")
  @AllureId("500024")
  @DisplayName("WEB: При регистрации возникает ошибка, если введено имя пользователя или пароль, содержащее пробелы")
  @Tag("WEB")
  void shouldShowErrorIfUsernameContainsWhiteSpace(String username, String password, String expectedMessage) {
    Selenide.open(LoginPage.URL, LoginPage.class)
        .doRegister()
        .fillRegisterPage(username, password, password)
        .errorSubmit()
        .checkAlertMessage(expectedMessage);
  }
}
