package guru.qa.niffler.controller.graphql;

import guru.qa.niffler.model.CurrencyJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.api.GrpcCurrencyClient;
import guru.qa.niffler.service.api.RestSpendClient;
import guru.qa.niffler.service.utils.GqlQueryPaginationAndSort;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@PreAuthorize("isAuthenticated()")
public class SpendQueryController {

  private final RestSpendClient restSpendClient;
  private final GrpcCurrencyClient grpcCurrencyClient;

  @Autowired
  public SpendQueryController(RestSpendClient restSpendClient, GrpcCurrencyClient grpcCurrencyClient) {
    this.restSpendClient = restSpendClient;
    this.grpcCurrencyClient = grpcCurrencyClient;
  }

  @QueryMapping
  public Slice<SpendJson> spends(@AuthenticationPrincipal Jwt principal,
                                 @Argument int page,
                                 @Argument int size,
                                 @Argument @Nullable List<String> sort,
                                 @Argument @Nullable String searchQuery,
                                 @Argument @Nullable DataFilterValues filterPeriod,
                                 @Argument @Nullable CurrencyValues filterCurrency) {
    return restSpendClient.getSpends(
        principal.getClaim("sub"),
        new GqlQueryPaginationAndSort(page, size, sort).pageable(),
        filterPeriod,
        filterCurrency,
        searchQuery
    );
  }

  @QueryMapping
  public SpendJson spend(@AuthenticationPrincipal Jwt principal,
                         @Argument String id) {
    final String username = principal.getClaim("sub");
    return restSpendClient.getSpend(
        id,
        username
    );
  }

  @QueryMapping
  public List<CurrencyJson> currencies() {
    return grpcCurrencyClient.getAllCurrencies();
  }
}
