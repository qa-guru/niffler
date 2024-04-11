package guru.qa.niffler.controller.pageable;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.api.RestSpendClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/spends")
public class SpendV2Controller {

    private final RestSpendClient restSpendClient;

    @Autowired
    public SpendV2Controller(RestSpendClient restSpendClient) {
        this.restSpendClient = restSpendClient;
    }

    @GetMapping("/all")
    public Page<SpendJson> getSpends(@AuthenticationPrincipal Jwt principal,
                                     @PageableDefault Pageable pageable,
                                     @RequestParam(required = false) DataFilterValues filterPeriod,
                                     @RequestParam(required = false) CurrencyValues filterCurrency) {
        String username = principal.getClaim("sub");
        return restSpendClient.getSpends(username, pageable, filterPeriod, filterCurrency);
    }
}
