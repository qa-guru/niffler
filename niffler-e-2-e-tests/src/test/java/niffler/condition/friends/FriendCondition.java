package niffler.condition.friends;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.impl.CollectionSource;
import niffler.model.rest.UserJson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nullable;
import java.util.List;

public class FriendCondition {

    public static CollectionCondition friends(List<UserJson> expectedFriends) {
        return new CollectionCondition() {
            @Override
            public void fail(CollectionSource collection, @Nullable List<WebElement> elements, @Nullable Exception lastError, long timeoutMs) {
                if (elements == null || elements.isEmpty()) {
                    ElementNotFound elementNotFound = new ElementNotFound(collection, List.of("Can`t find elements"), lastError);
                    throw elementNotFound;
                } else if (elements.size() != expectedFriends.size()) {
                    throw new FriendsSizeMismatch(collection, expectedFriends, bindElementsToFriend(elements), explanation, timeoutMs);
                } else {
                    throw new FriendsMismatch(collection, expectedFriends, bindElementsToFriend(elements), explanation, timeoutMs);
                }
            }

            @Override
            public boolean missingElementSatisfiesCondition() {
                return false;
            }

            @Override
            public boolean test(List<WebElement> elements) {
                if (elements.size() != expectedFriends.size()) {
                    return false;
                }
                for (int i = 0; i < expectedFriends.size(); i++) {
                    WebElement row = elements.get(i);
                    UserJson expectedFriend = expectedFriends.get(i);
                    List<WebElement> cells = row.findElements(By.cssSelector("td"));

                    if (!cells.get(1).getText().equals(expectedFriend.getUsername())) {
                        return false;
                    }
                }
                return true;
            }

            private List<UserJson> bindElementsToFriend(List<WebElement> elements) {
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
