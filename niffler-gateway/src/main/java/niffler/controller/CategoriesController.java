package niffler.controller;

import niffler.model.CategoryJson;
import niffler.service.api.RestSpendClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoriesController {

    private final RestSpendClient restSpendClient;

    @Autowired
    public CategoriesController(RestSpendClient restSpendClient) {
        this.restSpendClient = restSpendClient;
    }

    @GetMapping("/categories")
    public List<CategoryJson> getCategories(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return restSpendClient.getCategories(username);
    }

    @PostMapping("/category")
    public CategoryJson addCategory(@AuthenticationPrincipal Jwt principal,
                                    @RequestBody CategoryJson category) {
        String username = principal.getClaim("sub");
        category.setUsername(username);
        return restSpendClient.addCategory(category);
    }
}
