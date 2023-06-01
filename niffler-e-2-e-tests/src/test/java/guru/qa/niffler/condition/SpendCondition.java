package guru.qa.niffler.condition;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.SpendJson;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SpendCondition {

  public static CollectionCondition spends(SpendJson... expectedSpends) {
    return new CollectionCondition() {

      @Override
      public void fail(CollectionSource collection, @Nullable List<WebElement> elements,
          @Nullable Exception lastError, long timeoutMs) {
        if (elements == null || elements.isEmpty()) {
          throw new ElementNotFound(collection, toString(), timeoutMs, lastError);
        } else if (elements.size() != expectedSpends.length) {
          throw new SpendsSizeMismatch(collection,
              Arrays.asList(expectedSpends),
              elements.stream().map(SpendCondition::convertWebElementToSpendJson).toList(),
              explanation,
              timeoutMs);
        } else {
          throw new SpendsMismatch(
              collection,
              Arrays.asList(expectedSpends),
              elements.stream().map(SpendCondition::convertWebElementToSpendJson).toList(),
              explanation,
              timeoutMs);
        }
      }

      @Override
      public boolean missingElementSatisfiesCondition() {
        return false;
      }

      @Override
      public boolean test(List<WebElement> webElements) {
        if (webElements.size() != expectedSpends.length) {
          return false;
        }

        for (int i = 0; i < expectedSpends.length; i++) {
          WebElement row = webElements.get(i);
          SpendJson expectedSpend = expectedSpends[i];
          if (!spendRowEqulsSpend(row, expectedSpend)) {
            return false;
          }
        }
        return true;
      }
    };

  }

  private static boolean spendRowEqulsSpend(WebElement row, SpendJson expectedSpend) {
    return row.findElements(By.cssSelector("td")).get(1).getText()
        .equals(expectedSpend.getSpendDate().toString())
        && row.findElements(By.cssSelector("td")).get(2).getText()
        .equals(expectedSpend.getAmount().toString())
        && row.findElements(By.cssSelector("td")).get(3).getText()
        .equals(expectedSpend.getCurrency().toString())
        && row.findElements(By.cssSelector("td")).get(4).getText()
        .equals(expectedSpend.getCategory().toString())
        && row.findElements(By.cssSelector("td")).get(5).getText()
        .equals(expectedSpend.getDescription().toString());
  }

  private static SpendJson convertWebElementToSpendJson(WebElement row) {
    SpendJson sj = new SpendJson();
    sj.setSpendDate(null);
    sj.setAmount(Double.valueOf(row.findElements(By.cssSelector("td")).get(2).getText()));
    return sj;
  }

}
