package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.db.entity.userdata.CurrencyValues;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.Spend7Json;
import guru.qa.niffler.utils.DateUtils;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CategoryRustamCondition {

  public static CollectionCondition category(CategoryJson... expectedCategory) {
    return new CollectionCondition() {

      @Override
      public void fail(CollectionSource collection, @Nullable List<WebElement> elements,
          @Nullable Exception lastError, long timeoutMs) {
        if (elements == null || elements.isEmpty()) {
          throw new ElementNotFound(collection, List.of("Can`t find elements"), lastError);
        } else if (elements.size() != expectedCategory.length) {
          throw new CategoryRustamSizeMismatch(collection, Arrays.asList(expectedCategory),
              bindElementsToCategories(elements), explanation, timeoutMs);
        } else {
          throw new CategoryRustamMismatch(collection, Arrays.asList(expectedCategory),
              bindElementsToCategories(elements), explanation, timeoutMs);
        }
      }

      @Override
      public boolean test(List<WebElement> elements) {
        if (elements.size() != expectedCategory.length) {
          return false;
        }

        for (int i = 0; i < expectedCategory.length; i++) {
          WebElement row = elements.get(i);
          CategoryJson expectedCat = expectedCategory[i];
          List<WebElement> calls = row.findElements(By.cssSelector("td"));

          if (!calls.get(4).getText().equals(expectedCat.getCategory())) {
            return false;
          }
//          if (!calls.get(5).getText().equals(expectedCat.getUsername())) {
//            return false;
//          }
        }
        return true;
      }

      @Override
      public boolean missingElementSatisfiesCondition() {
        return false;
      }

      private List<CategoryJson> bindElementsToCategories(List<WebElement> elements) {
        return elements.stream()
            .map(e -> {
              List<WebElement> cells = e.findElements(By.cssSelector("td"));
              CategoryJson actual = new CategoryJson();
//              actual.setUsername(cells.get(5).getText());
              actual.setCategory(cells.get(4).getText());
              return actual;
            })
            .collect(Collectors.toList());
      }
    };
  }
}
