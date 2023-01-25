package niffler.service;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import niffler.model.CurrencyValues;
import niffler.model.DataFilterValues;
import niffler.model.StatisticJson;
import niffler.service.api.GrpcCurrencyClient;
import niffler.service.api.RestSpendClient;
import niffler.service.api.RestUserDataClient;
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
