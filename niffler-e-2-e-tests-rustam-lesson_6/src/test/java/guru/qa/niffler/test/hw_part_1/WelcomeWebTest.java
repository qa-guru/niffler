package guru.qa.niffler.test.hw_part_1;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.page.LoginPage.LOGIN_URL;
import static guru.qa.niffler.page.WelcomePage.WELCOME_PAGE_URL;

import com.github.javafaker.Faker;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.WelcomePage;
import guru.qa.niffler.test.BaseWebTest;
import org.junit.jupiter.api.Test;

public class WelcomeWebTest extends BaseWebTest {

  @Test
  void welcomeTest(){
    open(WELCOME_PAGE_URL, WelcomePage.class)
        .checkWelcomeFormWorks();
  }

  @Test
  void doLoginFromWelcomeTest(){
    open(WELCOME_PAGE_URL, WelcomePage.class)
        .checkThatPageLoaded()
        .doLogin()
        .checkThatPageLoaded()
        .fillSignInForm("rustam", "12345");
  }

  @Test
  void doRegisterFromWelcomeTest(){
    open(WELCOME_PAGE_URL, WelcomePage.class)
        .checkThatPageLoaded()
        .doRegister()
        .checkThatPageLoaded()
        .fillRegistrationForm(new Faker().name().username(), "12345", "12345");
  }

}
