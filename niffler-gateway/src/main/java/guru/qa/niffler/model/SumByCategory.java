package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public record SumByCategory(@JsonProperty("categoryName")
                            String categoryName,
                            @JsonProperty("currency")
                            CurrencyValues currency,
                            @JsonProperty("sum")
                            double sum,
                            @JsonProperty("firstSpendDate")
                            Date firstSpendDate,
                            @JsonProperty("lastSpendDate")
                            Date lastSpendDate) {
}
