package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.model.graphql.CreateCategoryInput;

import java.util.UUID;

public class CategoryJson {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("category")
    private String category;
    @JsonProperty("username")
    private String username;

    public CategoryJson() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public static CategoryJson fromCreateCategoryInput(CreateCategoryInput input) {
        CategoryJson categoryJson = new CategoryJson();
        categoryJson.setCategory(input.getCategory());
        return categoryJson;
    }
}
