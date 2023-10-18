package guru.qa.niffler.test.hw_part_1;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.page.LoginPage.LOGIN_URL;

import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.test.BaseWebTest;
import org.junit.jupiter.api.Test;

public class LoginWebTest extends BaseWebTest {

  @Test
  void loginTest(){
    open(LOGIN_URL, LoginPage.class)
        .checkThatPageLoaded()
        .fillSignInForm("rustam", "12345");
    new MainPage()
        .getHeader()
        .checkThatComponentDisplayed();
  }

  @Test
  void checkAllPagesFromHeaderIsDisplayedTest(){
    open(LOGIN_URL, LoginPage.class)
        .checkThatPageLoaded()
        .fillSignInForm("rustam", "12345");
    new MainPage()
        .getHeader()
        .checkThatComponentDisplayed()
        .goToFriendsPage()
        .getHeader()
        .checkThatComponentDisplayed()
        .goToAllPeoplePage()
        .getHeader()
        .checkThatComponentDisplayed()
        .goToProfilePage()
        .getHeader()
        .checkThatComponentDisplayed()
        .goToMainPage()
        .getHeader()
        .checkThatComponentDisplayed();


  }

}
