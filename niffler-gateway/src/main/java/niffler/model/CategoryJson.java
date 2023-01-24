package niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CategoryJson {
    @JsonProperty("description")
    private String description;

    public CategoryJson(String description) {
        this.description = description;
    }

    public CategoryJson() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
