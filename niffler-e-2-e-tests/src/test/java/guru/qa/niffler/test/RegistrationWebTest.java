package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.page.RegistrationPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.Isolated;
import org.junit.jupiter.api.parallel.ResourceLock;

@Execution(ExecutionMode.CONCURRENT)
public class RegistrationWebTest extends BaseWebTest {

  private RegistrationPage page = new RegistrationPage();

  @Test
  @ResourceLock("TEST_LOCK")
  public void errorMessageShouldBeVisibleInCaseThatPasswordsAreDifferent() {
    Selenide.open(RegistrationPage.URL);

    page.checkThatPageLoaded()
        .fillRegistrationForm("wdfsdasfs", "123", "12345")
        .checkErrorMessage("Passwords should be equal");
  }

  @Test
  @ResourceLock("TEST_LOCK")
  public void errorMessageShouldBeVisibleInCaseThatUsernameNotUniq() {
    final String username = "dima";

    Selenide.open(RegistrationPage.URL);
    page.checkThatPageLoaded()
        .fillRegistrationForm(username, "12345", "12345")
        .checkErrorMessage("Username `" + username + "` already exists");
  }

  @Test
  public void errorMessageShouldBeVisibleInCaseThatPasswordsLessThan3Symbols() {
    Selenide.open(RegistrationPage.URL);
    page.checkThatPageLoaded()
        .fillRegistrationForm("wdfsdadfdaasfs", "1", "1")
        .checkErrorMessage("Allowed password length should be from 3 to 12 characters");
  }

  @Test
  public void errorMessageShouldBeVisibleInCaseThatUsernameLessThan3Symbols() {
    Selenide.open(RegistrationPage.URL);
    page.checkThatPageLoaded()
        .fillRegistrationForm("g", "12345", "12345")
        .checkErrorMessage("Allowed username length should be from 3 to 50 characters");
  }
}
