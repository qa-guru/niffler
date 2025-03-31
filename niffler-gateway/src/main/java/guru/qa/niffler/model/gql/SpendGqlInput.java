package guru.qa.niffler.model.gql;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.validation.UnixEpochOrLater;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.UUID;

public record SpendGqlInput(
    UUID id,
    @NotNull(message = "Spend date can not be null")
    @UnixEpochOrLater(message = "Spend date must not be future or less than 01.01.1970")
    Date spendDate,
    @NotNull(message = "Category can not be null")
    @Valid CategoryGqlInput category,
    @NotNull(message = "Currency can not be null")
    CurrencyValues currency,
    @NotNull(message = "Amount can not be null")
    @DecimalMin(value = "0.01", message = "Amount should be greater than 0.01")
    Double amount,
    String description) {

}
