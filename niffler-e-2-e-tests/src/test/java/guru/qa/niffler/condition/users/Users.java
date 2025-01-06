package guru.qa.niffler.condition.users;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.TextsMismatch;
import com.codeborne.selenide.impl.CollectionSource;
import com.codeborne.selenide.impl.Html;
import guru.qa.niffler.model.rest.UserJson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.CheckResult.rejected;

@ParametersAreNonnullByDefault
public class Users extends WebElementsCondition {

  protected final List<String> expectedUsers;

  public Users(UserJson... expectedUsers) {
    this(Arrays.stream(expectedUsers).toList());
  }

  public Users(List<UserJson> expectedUsers) {
    if (expectedUsers.isEmpty()) {
      throw new IllegalArgumentException("No expected users given");
    }
    this.expectedUsers = expectedUsers.stream().map(UserJson::username).toList();
  }

  @Nonnull
  @Override
  public CheckResult check(Driver driver, List<WebElement> elements) {
    List<String> actualUsers = bindElementsToUsers(elements);
    if (actualUsers.size() != expectedUsers.size()) {
      String message = String.format("List size mismatch (expected: %s, actual: %s)", expectedUsers.size(), actualUsers.size());
      return rejected(message, actualUsers);
    }
    for (int i = 0; i < expectedUsers.size(); i++) {
      String expectedText = expectedUsers.get(i);
      String actualText = actualUsers.get(i);
      if (!check(actualText, expectedText)) {
        String message = String.format("Users #%s mismatch (expected: \"%s\", actual: \"%s\")", i, expectedText, actualText);
        return rejected(message, actualUsers);
      }
    }
    return CheckResult.accepted();
  }

  @CheckReturnValue
  protected boolean check(String actualText, String expectedText) {
    return Html.text.contains(actualText, expectedText);
  }

  @Override
  public void fail(CollectionSource collection, CheckResult lastCheckResult, @Nullable Exception cause, long timeoutMs) {
    List<String> actualTexts = lastCheckResult.getActualValue();
    if (actualTexts == null || actualTexts.isEmpty()) {
      throw new ElementNotFound(collection, toString(), timeoutMs, cause);
    }
    else {
      String message = lastCheckResult.getMessageOrElse(() -> "Users mismatch");
      throw new TextsMismatch(message, collection, expectedUsers, actualTexts, explanation, timeoutMs, cause);
    }
  }

  @Override
  public String toString() {
    return "Users " + expectedUsers;
  }

  private static List<String> bindElementsToUsers(List<WebElement> elements) {
    return elements.stream()
        .map(e -> {
          List<WebElement> cells = e.findElements(By.cssSelector("td"));
          return cells.get(1).getText();
        }).toList();
  }
}
