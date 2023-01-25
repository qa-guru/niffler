package niffler.controller;

import jakarta.validation.Valid;
import niffler.model.CurrencyValues;
import niffler.model.DataFilterValues;
import niffler.model.SpendJson;
import niffler.model.StatisticJson;
import niffler.service.StatisticAggregator;
import niffler.service.api.RestSpendClient;
import niffler.service.api.RestUserDataClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpendController {

    private final RestSpendClient restSpendClient;
    private final RestUserDataClient restUserDataClient;
    private final StatisticAggregator statisticAggregator;

    @Autowired
    public SpendController(RestSpendClient restSpendClient, RestUserDataClient restUserDataClient, StatisticAggregator statisticAggregator) {
        this.restSpendClient = restSpendClient;
        this.restUserDataClient = restUserDataClient;
        this.statisticAggregator = statisticAggregator;
    }

    @GetMapping("/spends")
    public List<SpendJson> getSpends(@AuthenticationPrincipal Jwt principal,
                                     @RequestParam(required = false) DataFilterValues filterPeriod,
                                     @RequestParam(required = false) CurrencyValues filterCurrency) {
        String username = principal.getClaim("sub");
        return restSpendClient.getSpends(username, filterPeriod, filterCurrency);
    }

    @PostMapping("/addSpend")
    @ResponseStatus(HttpStatus.CREATED)
    public SpendJson addSpend(@Valid @RequestBody SpendJson spend,
                              @AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        CurrencyValues userCurrency = restUserDataClient.currentUser(username).getCurrency();
        spend.setUsername(username);
        spend.setCurrency(userCurrency);

        return restSpendClient.addSpend(spend);
    }

    @GetMapping("/statistic")
    public List<StatisticJson> getTotalStatistic(@AuthenticationPrincipal Jwt principal,
                                                 @RequestParam(required = false) CurrencyValues filterCurrency,
                                                 @RequestParam(required = false) DataFilterValues filterPeriod) {
        String username = principal.getClaim("sub");
        return statisticAggregator.enrichStatisticRequest(username, filterCurrency, filterPeriod);
    }
}
