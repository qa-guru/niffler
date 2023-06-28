package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.CategoryEntity;

import java.util.UUID;

public class CategoryJson {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("category")
    private String category;
    @JsonProperty("username")
    private String username;

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

    public static CategoryJson fromEntity(CategoryEntity entity) {
        CategoryJson categoryJson = new CategoryJson();
        categoryJson.setId(entity.getId());
        categoryJson.setUsername(entity.getUsername());
        categoryJson.setCategory(entity.getCategory());
        return categoryJson;
    }
}
