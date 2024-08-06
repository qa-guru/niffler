package guru.qa.niffler.data.projection;

import guru.qa.niffler.model.CurrencyValues;

import java.util.Date;

public record SumByCategory(String categoryName,
                            CurrencyValues currency,
                            double sum,
                            Date firstSpendDate,
                            Date lastSpendDate) implements SumByCategoryInfo {
}
