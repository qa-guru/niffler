package guru.qa.niffler.condition.spend;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.utils.DateUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SpendCondition {

    public static CollectionCondition spends(SpendJson... expectedSpends) {
        return new CollectionCondition() {
            @Override
            public void fail(@Nonnull CollectionSource collection, @Nullable List<WebElement> elements, @Nullable Exception lastError, long timeoutMs) {
                if (elements == null || elements.isEmpty()) {
                    throw new ElementNotFound(collection, List.of("Can`t find elements"), lastError);
                } else if (elements.size() != expectedSpends.length) {
                    throw new SpendsSizeMismatch(collection, Arrays.asList(expectedSpends), bindElementsToSpends(elements), explanation, timeoutMs);
                } else {
                    throw new SpendsMismatch(collection, Arrays.asList(expectedSpends), bindElementsToSpends(elements), explanation, timeoutMs);
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
                    SpendJson expectedSpending = expectedSpends[i];
                    List<WebElement> cells = row.findElements(By.cssSelector("td"));

                    if (!cells.get(1).getText().equals(DateUtils.getDateAsString(expectedSpending.spendDate()))) {
                        return false;
                    }
                    if (!Double.valueOf(cells.get(2).getText()).equals(expectedSpending.amount())) {
                        return false;
                    }
                    if (!cells.get(3).getText().equals(expectedSpending.currency().name())) {
                        return false;
                    }
                    if (!cells.get(4).getText().equals(expectedSpending.category())) {
                        return false;
                    }
                    if (!cells.get(5).getText().equals(expectedSpending.description())) {
                        return false;
                    }
                }
                return true;
            }

            private List<SpendJson> bindElementsToSpends(List<WebElement> elements) {
                return elements.stream()
                        .map(e -> {
                            List<WebElement> cells = e.findElements(By.cssSelector("td"));
                            return new SpendJson(
                                    null,
                                    DateUtils.fromString(cells.get(1).getText()),
                                    Double.valueOf(cells.get(2).getText()),
                                    CurrencyValues.valueOf(cells.get(3).getText()),
                                    cells.get(4).getText(),
                                    cells.get(5).getText(),
                                    null
                            );
                        })
                        .collect(Collectors.toList());
            }
        };
    }
}
