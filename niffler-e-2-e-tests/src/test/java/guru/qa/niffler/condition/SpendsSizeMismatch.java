package guru.qa.niffler.condition;

import com.codeborne.selenide.ex.UIAssertionError;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.SpendJson;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static java.lang.System.lineSeparator;

@ParametersAreNonnullByDefault
public class SpendsSizeMismatch extends UIAssertionError {
    public SpendsSizeMismatch(CollectionSource collection,
                              List<SpendJson> expectedSpends, List<SpendJson> actualSpends,
                              @Nullable String explanation, long timeoutMs) {
        super(
                collection.driver(),
                "Spending size mismatch" +
                        lineSeparator() + "Actual: " + actualSpends + ", List size: " + actualSpends.size() +
                        lineSeparator() + "Expected: " + expectedSpends + ", List size: " + expectedSpends.size() +
                        (explanation == null ? "" : lineSeparator() + "Because: " + explanation) +
                        lineSeparator() + "Collection: " + collection.description(),
                expectedSpends, actualSpends,
                timeoutMs
        );
    }
}
