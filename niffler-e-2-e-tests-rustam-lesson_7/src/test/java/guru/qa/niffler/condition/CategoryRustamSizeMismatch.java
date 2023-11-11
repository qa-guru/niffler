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
public class CategoryRustamSizeMismatch extends UIAssertionError {
  public CategoryRustamSizeMismatch(CollectionSource collection,
      List<CategoryJson> expectedCategory, List<CategoryJson> actualCategory,
      @Nullable String explanation, long timeoutMs) {
    super(
        collection.driver(),
        "Category size mismatch" +
            lineSeparator() + "Actual: " + actualCategory + ", List size: " + actualCategory.size() +
            lineSeparator() + "Expected: " + expectedCategory + ", List size: " + expectedCategory.size() +
            (explanation == null ? "" : lineSeparator() + "Because: " + explanation) +
            lineSeparator() + "Collection: " + collection.description(),
        expectedCategory, actualCategory,
        timeoutMs
    );
  }
}
