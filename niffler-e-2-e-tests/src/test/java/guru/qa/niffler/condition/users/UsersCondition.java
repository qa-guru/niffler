package guru.qa.niffler.condition.users;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.rest.UserJson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nullable;
import java.util.List;

public class UsersCondition {

    public static CollectionCondition users(List<UserJson> expectedUsers) {
        return new CollectionCondition() {
            @Override
            public void fail(CollectionSource collection, @Nullable List<WebElement> elements, @Nullable Exception lastError, long timeoutMs) {
                if (elements == null || elements.isEmpty()) {
                    ElementNotFound elementNotFound = new ElementNotFound(collection, List.of("Can`t find elements"), lastError);
                    throw elementNotFound;
                } else if (elements.size() != expectedUsers.size()) {
                    throw new UsersSizeMismatch(collection, expectedUsers, bindElementsToUsers(elements), explanation, timeoutMs);
                } else {
                    throw new UsersMismatch(collection, expectedUsers, bindElementsToUsers(elements), explanation, timeoutMs);
                }
            }

            @Override
            public boolean missingElementSatisfiesCondition() {
                return false;
            }

            @Override
            public boolean test(List<WebElement> elements) {
                if (elements.size() != expectedUsers.size()) {
                    return false;
                }

                for (UserJson expectedUser : expectedUsers) {
                    boolean found = false;
                    for (WebElement row : elements) {
                        List<WebElement> cells = row.findElements(By.cssSelector("td"));
                        if (cells.get(1).getText().equals(expectedUser.getUsername())) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        return false;
                    }
                }

                return true;
            }

            private List<UserJson> bindElementsToUsers(List<WebElement> elements) {
                return elements.stream()
                        .map(e -> {
                            List<WebElement> cells = e.findElements(By.cssSelector("td"));
                            UserJson actual = new UserJson();
                            actual.setUsername(cells.get(1).getText());
                            return actual;
                        })
                        .toList();
            }
        };
    }
}
