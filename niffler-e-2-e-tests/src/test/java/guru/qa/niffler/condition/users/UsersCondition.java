package guru.qa.niffler.condition.users;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.rest.UserJson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

public class UsersCondition {

    public static WebElementsCondition users(UserJson... expectedUsers) {
        return new WebElementsCondition() {
            @Override
            public String toString() {
                return "Users " + Arrays.toString(expectedUsers);
            }

            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (elements.size() != expectedUsers.length) {
                    String message = String.format(
                            "Users table size mismatch (expected: %s, actual: %s)",
                            expectedUsers.length, elements.size()
                    );
                    return CheckResult.rejected(message, elements);
                }

                for (int i = 0; i < expectedUsers.length; i++) {
                    WebElement row = elements.get(i);
                    UserJson expectedUser = expectedUsers[i];
                    List<WebElement> cells = row.findElements(By.cssSelector("td"));
                    if (!cells.get(1).getText().equals(expectedUser.username())) {
                        String message = String.format(
                                "Username mismatch (expected: %s, actual: %s)",
                                expectedUser.username(), cells.get(1).getText()
                        );
                        return CheckResult.rejected(message, cells.get(1));
                    }
                }

                return CheckResult.accepted();
            }
        };
    }
}
