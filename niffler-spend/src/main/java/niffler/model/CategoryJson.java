package niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import niffler.data.CategoryEntity;

import java.util.UUID;

public class CategoryJson {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("description")
    private String description;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static CategoryJson fromEntity(CategoryEntity entity) {
        CategoryJson categoryJson = new CategoryJson();
        categoryJson.setId(entity.getId());
        categoryJson.setDescription(entity.getDescription());
        return categoryJson;
    }
}
