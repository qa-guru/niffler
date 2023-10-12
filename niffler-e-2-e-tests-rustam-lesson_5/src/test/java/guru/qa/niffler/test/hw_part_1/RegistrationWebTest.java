package guru.qa.niffler.test.hw_part_1;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.RegistrationPage;
import guru.qa.niffler.test.BaseWebTest;
import org.junit.jupiter.api.Test;

public class RegistrationWebTest extends BaseWebTest {

    @Test
    void errorMessageShouldBeVisibleInCaseThatPasswordsAreDifferent() {
        Selenide.open(RegistrationPage.REGISTRATION_URL, RegistrationPage.class)
            .checkThatPageLoaded()
            .fillRegistrationForm("rustam", "12345", "54321")
            .checkErrorMessage("Passwords should be equal");


        new FriendsPage()
            .getHeader()
            .goToMainPage()
            .getHeader()
            .goToFriendsPage();
    }

}
