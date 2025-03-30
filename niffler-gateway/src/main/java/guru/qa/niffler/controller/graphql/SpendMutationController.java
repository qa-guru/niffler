package guru.qa.niffler.controller.graphql;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.gql.CategoryGqlInput;
import guru.qa.niffler.model.gql.SpendGqlInput;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.api.GrpcCurrencyClient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Controller
@Validated
@PreAuthorize("isAuthenticated()")
public class SpendMutationController {

  private final SpendClient spendClient;

  @Autowired
  public SpendMutationController(SpendClient spendClient, GrpcCurrencyClient grpcCurrencyClient) {
    this.spendClient = spendClient;
  }

  @MutationMapping
  public SpendJson spend(@AuthenticationPrincipal Jwt principal,
                         @Valid @Argument SpendGqlInput input) {
    final  String principalUsername = principal.getClaim("sub");
    final SpendJson spendJson = SpendJson.fromSpendInput(input, principalUsername);
    return input.id() == null
        ? spendClient.addSpend(spendJson)
        : spendClient.editSpend(spendJson);
  }

  @MutationMapping
  public CategoryJson category(@AuthenticationPrincipal Jwt principal,
                               @Argument @Valid CategoryGqlInput input) {
    final  String principalUsername = principal.getClaim("sub");
    final CategoryJson categoryJson = CategoryJson.fromCategoryInput(input, principalUsername);
    return input.id() == null
        ? spendClient.addCategory(categoryJson)
        : spendClient.updateCategory(categoryJson);
  }

  @MutationMapping
  public List<String> deleteSpend(@AuthenticationPrincipal Jwt principal,
                                  @Argument @NotEmpty List<String> ids) {
    final String principalUsername = principal.getClaim("sub");
    spendClient.deleteSpends(principalUsername, ids);
    return ids;
  }
}
