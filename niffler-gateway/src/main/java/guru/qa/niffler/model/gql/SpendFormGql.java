package guru.qa.niffler.model.gql;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyJson;

import java.util.List;

public record SpendFormGql(List<CurrencyJson> currencies,
                           List<CategoryJson> categories) {
}
