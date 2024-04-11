package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.model.graphql.SpendInput;
import guru.qa.niffler.model.graphql.UpdateSpendInput;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.util.Date;
import java.util.UUID;

public record SpendJson(
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
        String description,
        @JsonProperty("username")
        String username) {

    public @Nonnull SpendJson addUsername(@Nonnull String username) {
        return new SpendJson(id, spendDate, category, currency, amount, description, username);
    }

    public static SpendJson fromSpendInput(@Nonnull SpendInput input) {
        return fromSpendInput(input, null);
    }

    public static SpendJson fromSpendInput(@Nonnull SpendInput input, @Nullable String username) {
        return new SpendJson(
                null,
                input.spendDate(),
                input.category(),
                input.currency(),
                input.amount(),
                input.description(),
                username
        );
    }

    public static @Nonnull SpendJson fromUpdateSpendInput(@Nonnull UpdateSpendInput input) {
        return fromUpdateSpendInput(input, null);
    }

    public static @Nonnull SpendJson fromUpdateSpendInput(@Nonnull UpdateSpendInput input, @Nullable String username) {
        return new SpendJson(
                input.id(),
                input.spendDate(),
                input.category(),
                input.currency(),
                input.amount(),
                input.description(),
                username
        );
    }
}
