package guru.qa.niffler.data.projection;

import guru.qa.niffler.model.CurrencyValues;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SumByCategoryAggregate implements SumByCategoryInfo {
  private final List<SumByCategoryInfo> delegates;

  public SumByCategoryAggregate(List<SumByCategoryInfo> delegates) {
    this.delegates = delegates;
  }

  @Override
  public String categoryName() {
    return delegates.getFirst().categoryName();
  }

  @Override
  public CurrencyValues currency() {
    return delegates.getFirst().currency();
  }

  @Override
  public double sum() {
    return BigDecimal.valueOf(
        delegates.stream().map(SumByCategoryInfo::sum).reduce(0.0, Double::sum)
    ).setScale(2, RoundingMode.HALF_UP).doubleValue();
  }

  @Override
  public Date firstSpendDate() {
    return Collections.min(delegates.stream().map(SumByCategoryInfo::firstSpendDate).toList());
  }

  @Override
  public Date lastSpendDate() {
    return Collections.max(delegates.stream().map(SumByCategoryInfo::lastSpendDate).toList());
  }
}
