package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.SpendEntity;
import jakarta.annotation.Nonnull;

import java.util.Date;
import java.util.UUID;

public record SpendJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("spendDate")
        Date spendDate,
        @JsonProperty("category")
        String category,
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("amount")
        Double amount,
        @JsonProperty("description")
        String description,
        @JsonProperty("username")
        String username) {

    public static @Nonnull SpendJson fromEntity(@Nonnull SpendEntity entity) {
        return new SpendJson(
                entity.getId(),
                entity.getSpendDate(),
                entity.getCategory().getCategory(),
                entity.getCurrency(),
                entity.getAmount(),
                entity.getDescription(),
                entity.getUsername()
        );
    }

}
