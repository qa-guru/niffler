package niffler.controller;

import niffler.model.CategoryJson;
import niffler.service.api.RestSpendClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
    public List<CategoryJson> getCategories() {
        return restSpendClient.getCategories();
    }
}
