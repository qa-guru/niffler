package guru.qa.niffler.data.projection;

import guru.qa.niffler.model.CurrencyValues;

import java.util.Date;

public class SumByCategoryInUserCurrency implements SumByCategoryInfo {
  private final SumByCategoryInfo delegate;
  private final CurrencyValues userCurrency;
  private final double sumInUserCurrency;

  public SumByCategoryInUserCurrency(SumByCategoryInfo delegate,
                                     CurrencyValues userCurrency,
                                     double sumInUserCurrency) {
    this.delegate = delegate;
    this.userCurrency = userCurrency;
    this.sumInUserCurrency = sumInUserCurrency;
  }

  @Override
  public String categoryName() {
    return delegate.categoryName();
  }

  @Override
  public CurrencyValues currency() {
    return userCurrency;
  }

  @Override
  public double sum() {
    return sumInUserCurrency;
  }

  @Override
  public Date firstSpendDate() {
    return delegate.firstSpendDate();
  }

  @Override
  public Date lastSpendDate() {
    return delegate.lastSpendDate();
  }
}
