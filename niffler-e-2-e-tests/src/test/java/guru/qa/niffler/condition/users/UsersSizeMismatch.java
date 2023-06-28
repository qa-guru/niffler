package guru.qa.niffler.condition.users;

import com.codeborne.selenide.ex.UIAssertionError;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.rest.UserJson;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static java.lang.System.lineSeparator;

@ParametersAreNonnullByDefault
public class UsersSizeMismatch extends UIAssertionError {
    public UsersSizeMismatch(CollectionSource collection,
                             List<UserJson> expectedUsers, List<UserJson> actualUsers,
                             @Nullable String explanation, long timeoutMs) {
        super(
                collection.driver(),
                "Users size mismatch" +
                        lineSeparator() + "Actual: " + actualUsers.stream().map(UserJson::getUsername).toList() + ", List size: " + actualUsers.size() +
                        lineSeparator() + "Expected: " + expectedUsers.stream().map(UserJson::getUsername).toList() + ", List size: " + expectedUsers.size() +
                        (explanation == null ? "" : lineSeparator() + "Because: " + explanation) +
                        lineSeparator() + "Collection: " + collection.description(),
                expectedUsers, actualUsers,
                timeoutMs
        );
    }
}
