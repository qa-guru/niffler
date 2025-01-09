package guru.qa.niffler.controller.graphql;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.gql.SpendFormGql;
import guru.qa.niffler.service.api.GrpcCurrencyClient;
import guru.qa.niffler.service.api.RestSpendClient;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
                                 @Argument @Nullable String searchQuery,
                                 @Argument DataFilterValues filterPeriod,
                                 @Argument CurrencyValues filterCurrency) {
    return restSpendClient.getSpends(
        principal.getClaim("sub"),
        PageRequest.of(page, size),
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
  public SpendFormGql spendForm(@AuthenticationPrincipal Jwt principal) {
    final String username = principal.getClaim("sub");
    final List<CurrencyJson> allCurrencies = grpcCurrencyClient.getAllCurrencies();
    final List<CategoryJson> categories = restSpendClient.getCategories(username, true);
    return new SpendFormGql(
        allCurrencies,
        categories
    );
  }
}
