package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrencyJson(
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("currencyRate")
        Double currencyRate) {
}
