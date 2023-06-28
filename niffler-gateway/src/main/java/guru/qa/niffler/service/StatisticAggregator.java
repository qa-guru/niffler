package guru.qa.niffler.service;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.StatisticJson;
import guru.qa.niffler.service.api.GrpcCurrencyClient;
import guru.qa.niffler.service.api.RestSpendClient;
import guru.qa.niffler.service.api.RestUserDataClient;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatisticAggregator {

    private final RestSpendClient restSpendClient;
    private final RestUserDataClient restUserDataClient;

    @Autowired
    public StatisticAggregator(RestSpendClient restSpendClient, RestUserDataClient restUserDataClient, GrpcCurrencyClient grpcCurrencyClient) {
        this.restSpendClient = restSpendClient;
        this.restUserDataClient = restUserDataClient;
    }

    public @Nonnull
    List<StatisticJson> enrichStatisticRequest(@Nonnull String username,
                                               @Nullable CurrencyValues filterCurrency,
                                               @Nullable DataFilterValues filterPeriod) {
        CurrencyValues userDefaultCurrency = restUserDataClient.currentUser(username).getCurrency();
        return restSpendClient.statistic(username, userDefaultCurrency, filterCurrency, filterPeriod);
    }
}
