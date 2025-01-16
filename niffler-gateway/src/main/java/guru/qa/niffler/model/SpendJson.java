package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.model.gql.SpendGqlInput;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.DecimalMin;
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
    @NotNull(message = "Category can not be null")
    CategoryJson category,
    @JsonProperty("currency")
    @NotNull(message = "Currency can not be null")
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
    return new SpendJson(
        id,
        spendDate,
        new CategoryJson(
            category.id(),
            category.name(),
            username,
            category.archived()
        ),
        currency,
        amount,
        description,
        username
    );
  }

  public static SpendJson fromSpendInput(@Nonnull SpendGqlInput input, @Nonnull String username) {
    return new SpendJson(
        input.id(),
        input.spendDate(),
        CategoryJson.fromCategoryInput(input.category(), username),
        input.currency(),
        input.amount(),
        input.description(),
        username
    );
  }
}
