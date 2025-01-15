package guru.qa.niffler.controller.graphql;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.gql.CategoryGqlInput;
import guru.qa.niffler.model.gql.SpendGqlInput;
import guru.qa.niffler.service.api.GrpcCurrencyClient;
import guru.qa.niffler.service.api.RestSpendClient;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@PreAuthorize("isAuthenticated()")
public class SpendMutationController {

  private final RestSpendClient restSpendClient;

  @Autowired
  public SpendMutationController(RestSpendClient restSpendClient, GrpcCurrencyClient grpcCurrencyClient) {
    this.restSpendClient = restSpendClient;
  }

  @MutationMapping
  public SpendJson spend(@AuthenticationPrincipal Jwt principal,
                         @Valid @Argument SpendGqlInput input) {
    final String username = principal.getClaim("sub");
    final SpendJson spendJson = SpendJson.fromSpendInput(input, username);
    return input.id() == null
        ? restSpendClient.addSpend(spendJson)
        : restSpendClient.editSpend(spendJson);
  }

  @MutationMapping
  public CategoryJson category(@AuthenticationPrincipal Jwt principal,
                               @Argument @Valid CategoryGqlInput input) {
    final String username = principal.getClaim("sub");
    final CategoryJson categoryJson = CategoryJson.fromCategoryInput(input, username);
    return input.id() == null
        ? restSpendClient.addCategory(categoryJson)
        : restSpendClient.updateCategory(categoryJson);
  }

  @MutationMapping
  public List<String> deleteSpend(@AuthenticationPrincipal Jwt principal,
                                  @Argument List<String> ids) {
    String username = principal.getClaim("sub");
    restSpendClient.deleteSpends(username, ids);
    return ids;
  }
}
