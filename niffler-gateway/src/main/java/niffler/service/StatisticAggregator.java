package niffler.service;

import jakarta.annotation.Nullable;
import niffler.model.CurrencyJson;
import niffler.model.CurrencyValues;
import niffler.model.StatisticJson;
import niffler.service.api.GrpcCurrencyClient;
import niffler.service.api.RestSpendClient;
import niffler.service.api.RestUserDataClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class StatisticAggregator {

    private final RestSpendClient restSpendClient;
    private final RestUserDataClient restUserDataClient;
    private final GrpcCurrencyClient grpcCurrencyClient;

    @Autowired
    public StatisticAggregator(RestSpendClient restSpendClient, RestUserDataClient restUserDataClient, GrpcCurrencyClient grpcCurrencyClient) {
        this.restSpendClient = restSpendClient;
        this.restUserDataClient = restUserDataClient;
        this.grpcCurrencyClient = grpcCurrencyClient;
    }

    public List<StatisticJson> enrichStatisticRequest(String username, @Nullable CurrencyValues filterCurrency, @Nullable Date from, @Nullable Date to) {
        CurrencyValues userDefaultCurrency = restUserDataClient.currentUser(username).getCurrency();
        List<CurrencyJson> currencyRates = grpcCurrencyClient.getAllCurrencies();
        return restSpendClient.statistic(username, currencyRates, userDefaultCurrency, filterCurrency, from, to);
    }
}
