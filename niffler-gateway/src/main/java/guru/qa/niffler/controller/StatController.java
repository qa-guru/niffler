package guru.qa.niffler.controller;

import guru.qa.niffler.config.NifflerGatewayServiceConfig;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.StatisticJson;
import guru.qa.niffler.service.StatisticAggregator;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stat")
@SecurityRequirement(name = NifflerGatewayServiceConfig.OPEN_API_AUTH_SCHEME)
public class StatController {

  private final StatisticAggregator statisticAggregator;

  @Autowired
  public StatController(StatisticAggregator statisticAggregator) {
    this.statisticAggregator = statisticAggregator;
  }

  @GetMapping("/total")
  public List<StatisticJson> getTotalStatistic(@AuthenticationPrincipal Jwt principal,
                                               @RequestParam(required = false) CurrencyValues statCurrency,
                                               @RequestParam(required = false) CurrencyValues filterCurrency,
                                               @RequestParam(required = false) DataFilterValues filterPeriod) {
    String username = principal.getClaim("sub");
    return statisticAggregator.enrichStatisticRequest(username, statCurrency, filterCurrency, filterPeriod);
  }
}
