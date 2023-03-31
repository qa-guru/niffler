package niffler.test;

import niffler.data.entity.UsersEntity;
import niffler.jupiter.DAOResolver;
import niffler.jupiter.extension.UserEntity;
import niffler.pages.HomePage;
import niffler.pages.LoginPage;
import niffler.pages.ProfilePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.text;
import static niffler.model.Currency.USD;


@ExtendWith({DAOResolver.class})
public class UserDataTest extends BaseTest {

    @UserEntity(username = "dima", currency = USD)
    UsersEntity dima;
    LoginPage loginPage = new LoginPage();
    HomePage homePage = new HomePage();
    ProfilePage profilePage = new ProfilePage();

    @BeforeEach
    void addUserDataBeforeTest() {
        loginPage.login("dima", "12345");
    }

    @Test
    void checkCurrencyTest() {
        homePage.usernameLink.click();
        profilePage.currencySelector.should(text(dima.getCurrency()));
    }
}
