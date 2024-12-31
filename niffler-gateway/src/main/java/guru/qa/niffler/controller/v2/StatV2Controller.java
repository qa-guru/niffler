package guru.qa.niffler.controller.v2;

import guru.qa.niffler.config.NifflerGatewayServiceConfig;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.StatisticV2Json;
import guru.qa.niffler.service.StatisticAggregator;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/stat")
@SecurityRequirement(name = NifflerGatewayServiceConfig.OPEN_API_AUTH_SCHEME)
public class StatV2Controller {

  private final StatisticAggregator statisticAggregator;

  @Autowired
  public StatV2Controller(StatisticAggregator statisticAggregator) {
    this.statisticAggregator = statisticAggregator;
  }

  @GetMapping("/total")
  public StatisticV2Json getTotalStatistic(@AuthenticationPrincipal Jwt principal,
                                           @RequestParam(required = false) CurrencyValues statCurrency,
                                           @RequestParam(required = false) CurrencyValues filterCurrency,
                                           @RequestParam(required = false) DataFilterValues filterPeriod) {
    String username = principal.getClaim("sub");
    return statisticAggregator.enrichStatisticRequestV2(username, statCurrency, filterCurrency, filterPeriod);
  }
}
