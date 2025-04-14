package guru.qa.niffler.service;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.StatisticJson;
import guru.qa.niffler.model.StatisticV2Json;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@Component
@ParametersAreNonnullByDefault
public class StatisticAggregator {

  private final SpendClient spendClient;
  private final UserDataClient userDataClient;

  @Autowired
  public StatisticAggregator(SpendClient spendClient,
                             UserDataClient userDataClient) {
    this.spendClient = spendClient;
    this.userDataClient = userDataClient;
  }

  public @Nonnull
  List<StatisticJson> enrichStatisticRequest(String username,
                                             @Nullable CurrencyValues userCurrency,
                                             @Nullable CurrencyValues filterCurrency,
                                             @Nullable DataFilterValues filterPeriod) {
    userCurrency = userCurrency == null
        ? userDataClient.currentUser(username).currency()
        : userCurrency;
    return spendClient.statistic(username, userCurrency, filterCurrency, filterPeriod);
  }

  public @Nonnull
  StatisticV2Json enrichStatisticRequestV2(String username,
                                           @Nullable CurrencyValues statCurrency,
                                           @Nullable CurrencyValues filterCurrency,
                                           @Nullable DataFilterValues filterPeriod) {
    statCurrency = statCurrency == null
        ? userDataClient.currentUser(username).currency()
        : statCurrency;
    return spendClient.statisticV2(username, statCurrency, filterCurrency, filterPeriod);
  }
}
