package niffler.condition.friends;

import com.codeborne.selenide.ex.UIAssertionError;
import com.codeborne.selenide.impl.CollectionSource;
import niffler.model.rest.UserJson;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static java.lang.System.lineSeparator;

@ParametersAreNonnullByDefault
public class FriendsSizeMismatch extends UIAssertionError {
    public FriendsSizeMismatch(CollectionSource collection,
                               List<UserJson> expectedFriends, List<UserJson> actualFriends,
                               @Nullable String explanation, long timeoutMs) {
        super(
                collection.driver(),
                "Friends size mismatch" +
                        lineSeparator() + "Actual: " + actualFriends.stream().map(UserJson::getUsername).toList() + ", List size: " + actualFriends.size() +
                        lineSeparator() + "Expected: " + expectedFriends.stream().map(UserJson::getUsername).toList() + ", List size: " + expectedFriends.size() +
                        (explanation == null ? "" : lineSeparator() + "Because: " + explanation) +
                        lineSeparator() + "Collection: " + collection.description(),
                expectedFriends, actualFriends,
                timeoutMs
        );
    }
}
