package guru.qa.niffler.controller;

import guru.qa.niffler.config.NifflerGatewayServiceConfig;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.SpendClient;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@SecurityRequirement(name = NifflerGatewayServiceConfig.OPEN_API_AUTH_SCHEME)
public class CategoriesController {

  private final SpendClient spendClient;

  @Autowired
  public CategoriesController(SpendClient spendClient) {
    this.spendClient = spendClient;
  }

  @GetMapping("/all")
  public List<CategoryJson> getCategories(@AuthenticationPrincipal Jwt principal,
                                          @RequestParam(required = false, defaultValue = "false") boolean excludeArchived) {
    String username = principal.getClaim("sub");
    return spendClient.getCategories(username, excludeArchived);
  }

  @PostMapping("/add")
  public CategoryJson addCategory(@AuthenticationPrincipal Jwt principal,
                                  @Valid @RequestBody CategoryJson category) {
    String username = principal.getClaim("sub");
    return spendClient.addCategory(new CategoryJson(
        category.id(),
        category.name(),
        username,
        category.archived()
    ));
  }

  @PatchMapping("/update")
  public CategoryJson updateCategory(@AuthenticationPrincipal Jwt principal,
                                     @RequestBody CategoryJson category) {
    String username = principal.getClaim("sub");
    return spendClient.updateCategory(new CategoryJson(
        category.id(),
        category.name(),
        username,
        category.archived()
    ));
  }
}
