package niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import niffler.data.CategoryEntity;

public class CategoryJson {
    @JsonProperty("description")
    private String description;

    public CategoryJson(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static CategoryJson fromEntity(CategoryEntity entity) {
        return new CategoryJson(entity.getDescription());
    }
}
