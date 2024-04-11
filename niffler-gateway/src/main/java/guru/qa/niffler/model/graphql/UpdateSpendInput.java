package guru.qa.niffler.model.graphql;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.model.CurrencyValues;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.util.Date;
import java.util.UUID;

public record UpdateSpendInput(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("spendDate")
        @NotNull(message = "Spend date can not be null")
        @PastOrPresent(message = "Spend date must not be future")
        Date spendDate,
        @JsonProperty("category")
        @NotBlank(message = "Category can not be blank")
        String category,
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("amount")
        @NotNull(message = "Amount can not be null")
        @DecimalMin(value = "0.01", message = "Amount should be greater than 0.01")
        Double amount,
        @JsonProperty("description")
        String description) {

}
