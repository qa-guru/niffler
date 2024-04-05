package guru.qa.niffler.condition.spend;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
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
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (elements.size() != expectedSpends.length) {
                    String message = String.format(
                            "Spends table size mismatch (expected: %s, actual: %s)",
                            expectedSpends.length, elements.size()
                    );
                    return CheckResult.rejected(message, elements);
                }
                for (int i = 0; i < expectedSpends.length; i++) {
                    WebElement row = elements.get(i);
                    SpendJson expectedSpending = expectedSpends[i];
                    List<WebElement> cells = row.findElements(By.cssSelector("td"));

                    if (!cells.get(1).getText().equals(DateUtils.getDateAsString(expectedSpending.spendDate()))) {
                        String message = String.format(
                                "Spend date mismatch (expected: %s, actual: %s)",
                                expectedSpending.spendDate(), cells.get(1).getText()
                        );
                        return CheckResult.rejected(message, cells.get(1));
                    }
                    if (!Double.valueOf(cells.get(2).getText()).equals(expectedSpending.amount())) {
                        String message = String.format(
                                "Spend amount mismatch (expected: %s, actual: %s)",
                                expectedSpending.amount(), cells.get(2).getText()
                        );
                        return CheckResult.rejected(message, cells.get(2));
                    }
                    if (!cells.get(3).getText().equals(expectedSpending.currency().name())) {
                        String message = String.format(
                                "Spend currency mismatch (expected: %s, actual: %s)",
                                expectedSpending.currency().name(), cells.get(3).getText()
                        );
                        return CheckResult.rejected(message, cells.get(3));
                    }
                    if (!cells.get(4).getText().equals(expectedSpending.category())) {
                        String message = String.format(
                                "Spend category mismatch (expected: %s, actual: %s)",
                                expectedSpending.category(), cells.get(4).getText()
                        );
                        return CheckResult.rejected(message, cells.get(4));
                    }
                    if (!cells.get(5).getText().equals(expectedSpending.description())) {
                        String message = String.format(
                                "Spend description mismatch (expected: %s, actual: %s)",
                                expectedSpending.description(), cells.get(5).getText()
                        );
                        return CheckResult.rejected(message, cells.get(5));
                    }
                }
                return CheckResult.accepted();
            }
        };
    }
}
