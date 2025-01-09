package guru.qa.niffler.model.gql;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;

import java.util.Date;
import java.util.UUID;

public record SpendGqlInput(
    UUID id,
    Date spendDate,
    CategoryJson category,
    CurrencyValues currency,
    Double amount,
    String description) {

}
