package guru.qa.niffler.condition;

import static java.lang.System.lineSeparator;

import com.codeborne.selenide.ex.UIAssertionError;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.SpendJson;
import java.util.List;
import javax.annotation.Nullable;

public class SpendsSizeMismatch extends UIAssertionError {

  public SpendsSizeMismatch(CollectionSource collection,
      List<SpendJson> expectedSpends, List<SpendJson> actualSpends,
      @Nullable String explanation, long timeoutMs) {
    super(
        collection.driver(),
        "Spends size mismatch" +
            lineSeparator() + "Actual: " + actualSpends + ", List size: " + actualSpends.size() +
            lineSeparator() + "Expected: " + expectedSpends + ", List size: "
            + expectedSpends.size() +
            (explanation == null ? "" : lineSeparator() + "Because: " + explanation) +
            lineSeparator() + "Collection: " + collection.description(),
        expectedSpends, actualSpends,
        timeoutMs
    );
  }
}
