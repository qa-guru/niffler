package guru.qa.niffler.controller.graphql;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.StatisticV2Json;
import guru.qa.niffler.service.StatisticAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

@Controller
@PreAuthorize("isAuthenticated()")
public class StatQueryController {

  private final StatisticAggregator statisticAggregator;

  @Autowired
  public StatQueryController(StatisticAggregator statisticAggregator) {
    this.statisticAggregator = statisticAggregator;
  }

  @QueryMapping
  public StatisticV2Json stat(@AuthenticationPrincipal Jwt principal,
                              @Argument CurrencyValues statCurrency,
                              @Argument CurrencyValues filterCurrency,
                              @Argument DataFilterValues filterPeriod) {
    String username = principal.getClaim("sub");
    return statisticAggregator.enrichStatisticRequestV2(
        username,
        statCurrency,
        filterCurrency,
        filterPeriod
    );
  }
}
