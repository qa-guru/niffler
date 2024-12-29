package guru.qa.niffler.controller.graphql;

import guru.qa.niffler.config.NifflerGatewayServiceConfig;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.graphql.CreateCategoryInput;
import guru.qa.niffler.service.api.RestSpendClient;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = NifflerGatewayServiceConfig.OPEN_API_AUTH_SCHEME)
public class CategoriesGraphqlController {

  private final RestSpendClient restSpendClient;

  @Autowired
  public CategoriesGraphqlController(RestSpendClient restSpendClient) {
    this.restSpendClient = restSpendClient;
  }

  @QueryMapping
  public List<CategoryJson> categories(@AuthenticationPrincipal Jwt principal) {
    String username = principal.getClaim("sub");
    return restSpendClient.getCategories(username, false);
  }

  @MutationMapping
  public CategoryJson createCategory(@AuthenticationPrincipal Jwt principal, @Argument @Valid CreateCategoryInput input) {
    String username = principal.getClaim("sub");
    return restSpendClient.addCategory(new CategoryJson(
        null,
        input.name(),
        username,
        input.archived()
    ));
  }
}
