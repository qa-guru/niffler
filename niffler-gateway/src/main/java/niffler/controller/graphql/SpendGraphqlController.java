package niffler.controller.graphql;

import jakarta.validation.Valid;
import niffler.model.CurrencyValues;
import niffler.model.DataFilterValues;
import niffler.model.SpendJson;
import niffler.model.graphql.SpendInput;
import niffler.model.graphql.UpdateSpendInput;
import niffler.service.StatisticAggregator;
import niffler.service.api.RestSpendClient;
import niffler.service.api.RestUserDataClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
public class SpendGraphqlController {

    private final RestSpendClient restSpendClient;
    private final RestUserDataClient restUserDataClient;
    private final StatisticAggregator statisticAggregator;

    @Autowired
    public SpendGraphqlController(RestSpendClient restSpendClient, RestUserDataClient restUserDataClient, StatisticAggregator statisticAggregator) {
        this.restSpendClient = restSpendClient;
        this.restUserDataClient = restUserDataClient;
        this.statisticAggregator = statisticAggregator;
    }

    @QueryMapping
    public List<SpendJson> spends(@AuthenticationPrincipal Jwt principal,
                                  @Argument DataFilterValues filterPeriod,
                                  @Argument CurrencyValues filterCurrency) {
        String username = principal.getClaim("sub");
        return restSpendClient.getSpends(username, filterPeriod, filterCurrency);
    }

    @MutationMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SpendJson addSpend(@Valid @Argument SpendInput spend,
                              @AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        CurrencyValues userCurrency = restUserDataClient.currentUser(username).getCurrency();
        SpendJson spendJson = SpendJson.fromSpendInput(spend);
        spendJson.setUsername(username);
        spendJson.setCurrency(userCurrency);
        return restSpendClient.addSpend(spendJson);
    }

    @MutationMapping
    public SpendJson updateSpend(@Valid @Argument UpdateSpendInput spend,
                                 @AuthenticationPrincipal Jwt principal) {
        if (spend.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id should be present");
        }
        String username = principal.getClaim("sub");
        SpendJson spendJson = SpendJson.fromUpdateSpendInput(spend);
        spendJson.setUsername(username);
        return restSpendClient.editSpend(spendJson);
    }

    @MutationMapping
    public List<String> deleteSpends(@AuthenticationPrincipal Jwt principal,
                                     @Argument List<String> ids) {
        String username = principal.getClaim("sub");
        restSpendClient.deleteSpends(username, ids);
        return ids;
    }
}
