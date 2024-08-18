package guru.qa.niffler.condition.spend;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.utils.DateUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class SpendCondition {

  public static WebElementsCondition spends(SpendJson... expectedSpends) {
    return new WebElementsCondition() {
      @Override
      public String toString() {
        return "Spends " + Arrays.toString(expectedSpends);
      }

      @Nonnull
      @Override
      public CheckResult check(@Nonnull Driver driver, @Nonnull List<WebElement> elements) {
        if (elements.size() != expectedSpends.length) {
          String message = String.format(
              "Spends table size mismatch (expected: %s, actual: %s)",
              expectedSpends.length, elements.size()
          );
          return CheckResult.rejected(message, bindElementsToSpends(elements));
        }
        for (int i = 0; i < expectedSpends.length; i++) {
          WebElement row = elements.get(i);
          SpendJson expectedSpending = expectedSpends[i];
          List<WebElement> cells = row.findElements(By.cssSelector("td"));

          final String formattedDate = DateUtils.getDateAsString(expectedSpending.spendDate(), "MMM d, yyyy");
          if (!cells.get(1).getText().equals(expectedSpending.category().name())) {
            String message = String.format(
                "Spend category mismatch (expected: %s, actual: %s)",
                expectedSpending.category().name(), cells.get(1).getText()
            );
            return CheckResult.rejected(message, bindElementsToSpends(elements));
          }
          if (cells.get(2).getText().equals(expectedSpending.amount() + " " + expectedSpending.currency().symbol)) {
            String message = String.format(
                "Spend amount or currency mismatch (expected: %s, actual: %s)",
                expectedSpending.amount(), cells.get(2).getText()
            );
            return CheckResult.rejected(message, bindElementsToSpends(elements));
          }
          if (!cells.get(3).getText().equals(expectedSpending.description())) {
            String message = String.format(
                "Spend description mismatch (expected: %s, actual: %s)",
                expectedSpending.description(), cells.get(3).getText()
            );
            return CheckResult.rejected(message, bindElementsToSpends(elements));
          }
          if (!cells.get(4).getText().equals(formattedDate)) {
            String message = String.format(
                "Spend date mismatch (expected: %s, actual: %s)",
                formattedDate, cells.get(4).getText()
            );
            return CheckResult.rejected(message, bindElementsToSpends(elements));
          }
        }
        return CheckResult.accepted();
      }
    };
  }

  private static List<SpendJson> bindElementsToSpends(List<WebElement> elements) {
    return elements.stream()
        .map(e -> {
          List<WebElement> cells = e.findElements(By.cssSelector("td"));
          String amount = cells.get(2).getText();
          Double amountValue = Double.valueOf(amount.split("\\s+")[0]);
          String amountSymbol = amount.split("\\s+")[1];
          return new SpendJson(
              null,
              DateUtils.fromString(cells.get(4).getText(), "MMM d, yyyy"),
              amountValue,
              CurrencyValues.fromSymbol(amountSymbol),
              new CategoryJson(
                  null,
                  cells.get(1).getText(),
                  null,
                  false
              ),
              cells.get(3).getText(),
              null
          );
        })
        .toList();
  }
}
