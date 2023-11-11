package guru.qa.niffler.condition;

import static java.lang.System.lineSeparator;

import com.codeborne.selenide.ex.UIAssertionError;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.Spend7Json;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CategoryRustamMismatch extends UIAssertionError {
  public CategoryRustamMismatch(CollectionSource collection,
      List<CategoryJson> expectedCategory, List<CategoryJson> actualCategory,
      @Nullable String explanation, long timeoutMs) {
    super(
        collection.driver(),
        "Category mismatch" +
            lineSeparator() + "Actual: " + actualCategory +
            lineSeparator() + "Expected: " + expectedCategory +
            (explanation == null ? "" : lineSeparator() + "Because: " + explanation) +
            lineSeparator() + "Collection: " + collection.description(),
        expectedCategory, actualCategory,
        timeoutMs);
  }
}
