package niffler.condition.friends;

import com.codeborne.selenide.ex.UIAssertionError;
import com.codeborne.selenide.impl.CollectionSource;
import niffler.model.rest.UserJson;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static java.lang.System.lineSeparator;

@ParametersAreNonnullByDefault
public class FriendsMismatch extends UIAssertionError {
    public FriendsMismatch(CollectionSource collection,
                           List<UserJson> expectedFriends, List<UserJson> actualFriends,
                           @Nullable String explanation, long timeoutMs) {
        super(
                collection.driver(),
                "Friends mismatch" +
                        lineSeparator() + "Actual: " + actualFriends.stream().map(UserJson::getUsername).toList() +
                        lineSeparator() + "Expected: " + expectedFriends.stream().map(UserJson::getUsername).toList() +
                        (explanation == null ? "" : lineSeparator() + "Because: " + explanation) +
                        lineSeparator() + "Collection: " + collection.description(),
                expectedFriends, actualFriends,
                timeoutMs);
    }
}
