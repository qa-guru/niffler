package guru.qa.niffler.condition;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.db.entity.userdata.CurrencyValues;
import guru.qa.niffler.model.Spend7Json;
import guru.qa.niffler.utils.DateUtils;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SpendRustamCondition {

  public static CollectionCondition spends(Spend7Json... expectedSpends) {
    return new CollectionCondition() {

      @Override
      public void fail(CollectionSource collection, @Nullable List<WebElement> elements,
          @Nullable Exception lastError, long timeoutMs) {
        if (elements == null || elements.isEmpty()) {
          throw new ElementNotFound(collection, List.of("Can`t find elements"), lastError);
        } else if (elements.size() != expectedSpends.length) {
          throw new SpendsRustamSizeMismatch(collection, Arrays.asList(expectedSpends),
              bindElementsToSpends(elements), explanation, timeoutMs);
        } else {
          throw new SpendsRustamMismatch(collection, Arrays.asList(expectedSpends),
              bindElementsToSpends(elements), explanation, timeoutMs);
        }
      }

      @Override
      public boolean missingElementSatisfiesCondition() {
        return false;
      }

      @Override
      public boolean test(List<WebElement> elements) {
        if (elements.size() != expectedSpends.length) {
          return false;
        }

        for (int i = 0; i < expectedSpends.length; i++) {
          WebElement row = elements.get(i);
          Spend7Json expectedSpending = expectedSpends[i];
          List<WebElement> calls = row.findElements(By.cssSelector("td"));

          if (!calls.get(1).getText().equals(DateUtils.getDateAsString(expectedSpending.getSpendDate()))) {
            return false;
          }
          if (!Double.valueOf(calls.get(2).getText()).equals(expectedSpending.getAmount())) {
            return false;
          }
          if (!calls.get(3).getText().equals(expectedSpending.getCurrency().name())) {
            return false;
          }
          if (!calls.get(4).getText().equals(expectedSpending.getCategory())) {
            return false;
          }
          if (!calls.get(5).getText().equals(expectedSpending.getDescription())) {
            return false;
          }
        }
        return true;
      }

      private List<Spend7Json> bindElementsToSpends(List<WebElement> elements) {
        return elements.stream()
            .map(e -> {
              List<WebElement> cells = e.findElements(By.cssSelector("td"));
              Spend7Json actual = new Spend7Json();
              actual.setSpendDate(DateUtils.fromString(cells.get(1).getText()));
              actual.setAmount(Double.valueOf(cells.get(2).getText()));
              actual.setCurrency(CurrencyValues.valueOf(cells.get(3).getText()));
              actual.setCategory(cells.get(4).getText());
              actual.setDescription(cells.get(5).getText());
              return actual;
            })
            .collect(Collectors.toList());
      }
    };
  }
}
