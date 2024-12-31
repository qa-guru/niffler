package guru.qa.niffler.controller.graphql;

import guru.qa.niffler.config.NifflerGatewayServiceConfig;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.graphql.SpendInput;
import guru.qa.niffler.model.graphql.UpdateSpendInput;
import guru.qa.niffler.service.StatisticAggregator;
import guru.qa.niffler.service.UserDataClient;
import guru.qa.niffler.service.api.RestSpendClient;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = NifflerGatewayServiceConfig.OPEN_API_AUTH_SCHEME)
public class SpendGraphqlController {

  private final RestSpendClient restSpendClient;
  private final UserDataClient userDataClient;
  private final StatisticAggregator statisticAggregator;

  @Autowired
  public SpendGraphqlController(RestSpendClient restSpendClient,
                                UserDataClient userDataClient,
                                StatisticAggregator statisticAggregator) {
    this.restSpendClient = restSpendClient;
    this.userDataClient = userDataClient;
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
    CurrencyValues userCurrency = userDataClient.currentUser(username).currency();
    if (userCurrency != spend.currency()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Spending currency should be same with user currency");
    }
    SpendJson spendJson = SpendJson.fromSpendInput(spend, username);
    return restSpendClient.addSpend(spendJson);
  }

  @MutationMapping
  public SpendJson updateSpend(@Valid @Argument UpdateSpendInput spend,
                               @AuthenticationPrincipal Jwt principal) {
    if (spend.id() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id should be present");
    }
    String username = principal.getClaim("sub");
    SpendJson spendJson = SpendJson.fromUpdateSpendInput(spend, username);
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
