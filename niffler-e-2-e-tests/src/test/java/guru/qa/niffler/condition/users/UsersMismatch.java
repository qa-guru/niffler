package guru.qa.niffler.condition.users;

import com.codeborne.selenide.ex.UIAssertionError;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.rest.UserJson;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static java.lang.System.lineSeparator;

@ParametersAreNonnullByDefault
public class UsersMismatch extends UIAssertionError {
    public UsersMismatch(CollectionSource collection,
                         List<UserJson> expectedUsers, List<UserJson> actualUsers,
                         @Nullable String explanation, long timeoutMs) {
        super(
                collection.driver(),
                "Users mismatch" +
                        lineSeparator() + "Actual: " + actualUsers.stream().map(UserJson::getUsername).toList() +
                        lineSeparator() + "Expected: " + expectedUsers.stream().map(UserJson::getUsername).toList() +
                        (explanation == null ? "" : lineSeparator() + "Because: " + explanation) +
                        lineSeparator() + "Collection: " + collection.description(),
                expectedUsers, actualUsers,
                timeoutMs);
    }
}
