package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record StatisticV2Json(
    @JsonProperty("total")
    Double total,
    @JsonProperty("currency")
    CurrencyValues currency,
    @JsonProperty("statByCategories")
    List<SumByCategory> statByCategories) {

}
