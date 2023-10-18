package guru.qa.niffler.test.hw_part_1;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.page.WelcomePageRustam.WELCOME_PAGE_URL;

import com.github.javafaker.Faker;
import guru.qa.niffler.page.WelcomePageRustam;
import guru.qa.niffler.test.BaseWebTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class WelcomeWebTestRustam extends BaseWebTest {

  @Test
  void welcomeTest(){
    open(WELCOME_PAGE_URL, WelcomePageRustam.class)
        .checkWelcomeFormWorks();
  }

  @Test
  void doLoginFromWelcomeTest(){
    open(WELCOME_PAGE_URL, WelcomePageRustam.class)
        .checkThatPageLoaded()
        .doLogin()
        .checkThatPageLoaded()
        .fillSignInForm("rustam", "12345");
  }

  @Test
  void doRegisterFromWelcomeTest(){
    open(WELCOME_PAGE_URL, WelcomePageRustam.class)
        .checkThatPageLoaded()
        .doRegister()
        .checkThatPageLoaded()
        .fillRegistrationForm(new Faker().name().username(), "12345", "12345");
  }

  @Test
  @Disabled
  void disabledTest(){
  }

}
