package guru.qa.niffler.test.hw_part_1;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.page.LoginPageRustam.LOGIN_URL;
import static guru.qa.niffler.page.WelcomePageRustam.WELCOME_PAGE_URL;

import guru.qa.niffler.page.LoginPageRustam;
import guru.qa.niffler.page.MainPageRustam;
import guru.qa.niffler.page.WelcomePageRustam;
import guru.qa.niffler.test.BaseWebTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class LoginWebTestRustam extends BaseWebTest {

  @Test
  void loginTest(){
    open(LOGIN_URL, LoginPageRustam.class)
        .checkThatPageLoaded()
        .fillSignInForm("rustam", "12345");
    new MainPageRustam()
        .checkThatPageLoaded()
        .getHeader()
        .checkThatComponentDisplayed();
  }

  @Test
  void checkAllPagesFromHeaderIsDisplayedTest(){
    open(WELCOME_PAGE_URL, WelcomePageRustam.class)
        .doLogin()
        .checkThatPageLoaded()
        .fillSignInForm("rustam", "12345");
    new MainPageRustam()
        .checkThatPageLoaded()
        .getHeader()
        .goToFriendsPage()
        .checkThatPageLoaded()
        .getHeader()
        .goToAllPeoplePage()
        .checkThatPageLoaded()
        .getHeader()
        .goToProfilePage()
        .checkThatPageLoaded()
        .getHeader()
        .goToMainPage()
        .checkThatPageLoaded();
  }

  @Test
  void checkLogoutTest(){
    open(WELCOME_PAGE_URL, WelcomePageRustam.class)
        .doLogin()
        .checkThatPageLoaded()
        .fillSignInForm("rustam", "12345");
    new MainPageRustam()
        .checkThatPageLoaded()
        .doLogout();
    new WelcomePageRustam()
        .checkThatPageLoaded();
  }

  @CsvSource(value = {
      "qwertyqwerty| 12345",
      "rustam| qwerty"
  }, delimiter = '|')
  @ParameterizedTest
  void errorMessageShouldBeVisibleInCaseIfLoginIncorrect(String login, String password){
    open(WELCOME_PAGE_URL, WelcomePageRustam.class)
        .doLogin()
        .checkThatPageLoaded()
        .fillSignInForm(login, password)
        .checkErrorMessage("Неверные учетные данные пользователя");
  }

  @Test
  void goToRegisterPageFromLinkInLoginPageTest(){
    open(WELCOME_PAGE_URL, WelcomePageRustam.class)
        .doLogin()
        .checkThatPageLoaded()
        .goToRegisterPage()
        .checkThatPageLoaded();
  }

}
