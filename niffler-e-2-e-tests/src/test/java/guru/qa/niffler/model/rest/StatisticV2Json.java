package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record StatisticV2Json(
    @JsonProperty("total")
    Double total,
    @JsonProperty("currency")
    CurrencyValues currency,
    @JsonProperty("statByCategories")
    List<SumByCategory> statByCategories) {

}
