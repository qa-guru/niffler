package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ParseSpendingResponse(
    @JsonProperty("amount")
    double amount,
    
    @JsonProperty("category")
    String category,
    
    @JsonProperty("description")
    String description,
    
    @JsonProperty("currency")
    String currency,
    
    @JsonProperty("spendDate")
    String spendDate
) {
}

