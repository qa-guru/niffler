package guru.qa.niffler.controller;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.StatisticJson;
import guru.qa.niffler.service.StatisticAggregator;
import guru.qa.niffler.service.api.RestSpendClient;
import guru.qa.niffler.service.api.RestUserDataClient;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

    @PatchMapping("/editSpend")
    public SpendJson editSpend(@Valid @RequestBody SpendJson spend,
                               @AuthenticationPrincipal Jwt principal) {
        if (spend.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id should be present");
        }
        String username = principal.getClaim("sub");
        spend.setUsername(username);

        return restSpendClient.editSpend(spend);
    }

    @GetMapping("/statistic")
    public List<StatisticJson> getTotalStatistic(@AuthenticationPrincipal Jwt principal,
                                                 @RequestParam(required = false) CurrencyValues filterCurrency,
                                                 @RequestParam(required = false) DataFilterValues filterPeriod) {
        String username = principal.getClaim("sub");
        return statisticAggregator.enrichStatisticRequest(username, filterCurrency, filterPeriod);
    }

    @DeleteMapping("/deleteSpends")
    public ResponseEntity<Void> deleteSpends(@AuthenticationPrincipal Jwt principal,
                                             @RequestParam List<String> ids) {
        String username = principal.getClaim("sub");
        return new ResponseEntity<>(restSpendClient.deleteSpends(username, ids));
    }
}
