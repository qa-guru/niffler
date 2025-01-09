package guru.qa.niffler.condition.spend;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.TextsMismatch;
import com.codeborne.selenide.impl.CollectionSource;
import com.codeborne.selenide.impl.Html;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.utils.DateUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.CheckResult.rejected;

@ParametersAreNonnullByDefault
public class Spends extends WebElementsCondition {

  protected final List<String> expectedSpends;

  public Spends(SpendJson... expectedSpends) {
    this(Arrays.stream(expectedSpends).toList());
  }

  public Spends(List<SpendJson> expectedSpends) {
    if (expectedSpends.isEmpty()) {
      throw new IllegalArgumentException("No expected spends given");
    }
    this.expectedSpends = expectedSpends.stream().map(SpendJson::toSimpleString).toList();
  }

  @Nonnull
  @Override
  public CheckResult check(Driver driver, List<WebElement> elements) {
    List<String> actualSpends = bindElementsToSpends(elements);
    if (actualSpends.size() != expectedSpends.size()) {
      String message = String.format("List size mismatch (expected: %s, actual: %s)", expectedSpends.size(), actualSpends.size());
      return rejected(message, actualSpends);
    }
    for (int i = 0; i < expectedSpends.size(); i++) {
      String expectedText = expectedSpends.get(i);
      String actualText = actualSpends.get(i);
      if (!check(actualText, expectedText)) {
        String message = String.format("Spend #%s mismatch (expected: \"%s\", actual: \"%s\")", i, expectedText, actualText);
        return rejected(message, actualSpends);
      }
    }
    return CheckResult.accepted();
  }

  @CheckReturnValue
  protected boolean check(String actualText, String expectedText) {
    return Html.text.equals(actualText, expectedText);
  }

  @Override
  public void fail(CollectionSource collection, CheckResult lastCheckResult, @Nullable Exception cause, long timeoutMs) {
    List<String> actualTexts = lastCheckResult.getActualValue();
    if (actualTexts == null || actualTexts.isEmpty()) {
      throw new ElementNotFound(collection, toString(), timeoutMs, cause);
    } else {
      String message = lastCheckResult.getMessageOrElse(() -> "Spends mismatch");
      throw new TextsMismatch(message, collection, expectedSpends, actualTexts, explanation, timeoutMs, cause);
    }
  }

  @Override
  public String toString() {
    return "Spends " + expectedSpends;
  }

  private static List<String> bindElementsToSpends(List<WebElement> elements) {
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
          ).toSimpleString();
        })
        .toList();
  }
}
