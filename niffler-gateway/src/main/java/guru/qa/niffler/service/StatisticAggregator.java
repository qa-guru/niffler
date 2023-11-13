package guru.qa.niffler.service;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.StatisticJson;
import guru.qa.niffler.service.api.RestSpendClient;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatisticAggregator {

    private final RestSpendClient restSpendClient;
    private final UserDataClient userDataClient;

    @Autowired
    public StatisticAggregator(RestSpendClient restSpendClient,
                               UserDataClient userDataClient) {
        this.restSpendClient = restSpendClient;
        this.userDataClient = userDataClient;
    }

    public @Nonnull
    List<StatisticJson> enrichStatisticRequest(@Nonnull String username,
                                               @Nullable CurrencyValues filterCurrency,
                                               @Nullable DataFilterValues filterPeriod) {
        CurrencyValues userDefaultCurrency = userDataClient.currentUser(username).currency();
        return restSpendClient.statistic(username, userDefaultCurrency, filterCurrency, filterPeriod);
    }
}
