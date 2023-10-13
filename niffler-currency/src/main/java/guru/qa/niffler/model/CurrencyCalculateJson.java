package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.CurrencyValues;

public record CurrencyCalculateJson(
        @JsonProperty("originalCurrency")
        CurrencyValues originalCurrency,
        @JsonProperty("desiredCurrency")
        CurrencyValues desiredCurrency,
        @JsonProperty("originalAmount")
        Double originalAmount,
        @JsonProperty("desiredAmount")
        Double desiredAmount) {

}
