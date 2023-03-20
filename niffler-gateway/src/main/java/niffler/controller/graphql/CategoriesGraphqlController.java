package niffler.controller.graphql;

import jakarta.validation.Valid;
import niffler.model.CategoryJson;
import niffler.model.graphql.CreateCategoryInput;
import niffler.service.api.RestSpendClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CategoriesGraphqlController {

    private final RestSpendClient restSpendClient;

    @Autowired
    public CategoriesGraphqlController(RestSpendClient restSpendClient) {
        this.restSpendClient = restSpendClient;
    }

    @QueryMapping
    public List<CategoryJson> categories(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return restSpendClient.getCategories(username);
    }

    @MutationMapping
    public CategoryJson createCategory(@AuthenticationPrincipal Jwt principal, @Argument @Valid CreateCategoryInput input) {
        String username = principal.getClaim("sub");
        CategoryJson category = CategoryJson.fromCreateCategoryInput(input);
        category.setUsername(username);
        return restSpendClient.addCategory(category);
    }
}
