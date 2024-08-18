package guru.qa.niffler.data.projection;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.model.CurrencyValues;

import java.util.Date;

public interface SumByCategoryInfo {
  @JsonProperty("categoryName")
  String categoryName();

  @JsonProperty("currency")
  CurrencyValues currency();

  @JsonProperty("sum")
  double sum();

  @JsonProperty("firstSpendDate")
  Date firstSpendDate();

  @JsonProperty("lastSpendDate")
  Date lastSpendDate();
}
