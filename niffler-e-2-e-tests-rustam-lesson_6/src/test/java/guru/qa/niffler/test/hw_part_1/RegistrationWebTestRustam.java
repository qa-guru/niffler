package guru.qa.niffler.test.hw_part_1;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.page.FriendsPageRustam;
import guru.qa.niffler.page.RegistrationPageRustam;
import guru.qa.niffler.test.BaseWebTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

public class RegistrationWebTestRustam extends BaseWebTest {

    @Test
    void errorMessageShouldBeVisibleInCaseThatPasswordsAreDifferent() {
        Selenide.open(RegistrationPageRustam.REGISTRATION_URL, RegistrationPageRustam.class)
            .checkThatPageLoaded()
            .fillRegistrationForm("rustam", "12345", "54321")
            .checkErrorMessage("Passwords should be equal");
    }

    @Test
    @ResourceLock("lock")
    void errorMessageShouldBeVisibleInCaseIfUserAlreadyExist() {
        Selenide.open(RegistrationPageRustam.REGISTRATION_URL, RegistrationPageRustam.class)
            .checkThatPageLoaded()
            .fillRegistrationForm("rustam", "12345", "12345")
            .checkErrorMessage("Username `rustam` already exists");
    }

    @Test
    void errorMessageShouldBeVisibleInCaseIfPasswordLessThen3() {
        Selenide.open(RegistrationPageRustam.REGISTRATION_URL, RegistrationPageRustam.class)
            .checkThatPageLoaded()
            .fillRegistrationForm(new Faker().name().username(), "12", "12")
            .checkErrorMessage("Allowed password length should be from 3 to 12 characters");
    }

    @Test
    void errorMessageShouldBeVisibleInCaseIfPasswordMoreThen12() {
        Selenide.open(RegistrationPageRustam.REGISTRATION_URL, RegistrationPageRustam.class)
            .checkThatPageLoaded()
            .fillRegistrationForm(new Faker().name().username(), "1234567890123", "1234567890123")
            .checkErrorMessage("Allowed password length should be from 3 to 12 characters");
    }

    @Test
    @ResourceLock("lock")
    void errorMessageShouldBeVisibleInCaseIfLoginMoreThen50() {
        Selenide.open(RegistrationPageRustam.REGISTRATION_URL, RegistrationPageRustam.class)
            .checkThatPageLoaded()
            .fillRegistrationForm("123456789012345678901234567890123456789012345678901", "12345", "12345")
            .checkErrorMessage("Username can`t be longer than 50 characters");
    }

}
