package guru.qa.niffler.controller;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.api.RestSpendClient;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {

    private final RestSpendClient restSpendClient;

    @Autowired
    public CategoriesController(RestSpendClient restSpendClient) {
        this.restSpendClient = restSpendClient;
    }

    @GetMapping("/all")
    public List<CategoryJson> getCategories(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return restSpendClient.getCategories(username);
    }

    @PostMapping("/add")
    public CategoryJson addCategory(@AuthenticationPrincipal Jwt principal,
                                    @Valid @RequestBody CategoryJson category) {
        String username = principal.getClaim("sub");
        return restSpendClient.addCategory(new CategoryJson(
                category.id(),
                category.category(),
                username
        ));
    }
}
