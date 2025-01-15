package guru.qa.niffler.model.gql;

import guru.qa.niffler.model.CurrencyValues;

import java.util.Date;
import java.util.UUID;

public record SpendGqlInput(
    UUID id,
    Date spendDate,
    CategoryGqlInput category,
    CurrencyValues currency,
    Double amount,
    String description) {

}
