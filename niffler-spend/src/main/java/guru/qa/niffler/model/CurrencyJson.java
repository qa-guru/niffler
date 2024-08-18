package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrencyJson(
    @JsonProperty("currency")
    CurrencyValues currency,
    @JsonProperty("currencyRate")
    Double currencyRate) {

}
