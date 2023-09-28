package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.UUID;

public record SpendJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("spendDate")
        Date spendDate,
        @JsonProperty("amount")
        Double amount,
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("category")
        String category,
        @JsonProperty("description")
        String description,
        @JsonProperty("username")
        String username) {

    public SpendJson addUsername(String username) {
        return new SpendJson(id, spendDate, amount, currency, category, description, username);
    }
}
