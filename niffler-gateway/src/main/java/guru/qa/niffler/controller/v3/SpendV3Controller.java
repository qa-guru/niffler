package guru.qa.niffler.controller.v3;

import guru.qa.niffler.config.NifflerGatewayServiceConfig;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v3/spends")
@SecurityRequirement(name = NifflerGatewayServiceConfig.OPEN_API_AUTH_SCHEME)
public class SpendV3Controller {

  private final SpendClient spendClient;

  @Autowired
  public SpendV3Controller(SpendClient spendClient) {
    this.spendClient = spendClient;
  }

  @GetMapping("/all")
  public PagedModel<SpendJson> getSpends(@AuthenticationPrincipal Jwt principal,
                                         @PageableDefault Pageable pageable,
                                         @RequestParam(required = false) DataFilterValues filterPeriod,
                                         @RequestParam(required = false) CurrencyValues filterCurrency,
                                         @RequestParam(required = false) String searchQuery) {
    String username = principal.getClaim("sub");
    return spendClient.getSpendsV3(username, pageable, filterPeriod, filterCurrency, searchQuery);
  }
}
