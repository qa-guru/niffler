package niffler.model.graphql;

import jakarta.validation.constraints.NotNull;

public class CreateCategoryInput {
    @NotNull(message = "Category name can not be null")
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "CreateCategoryInput{" +
                "category='" + category + '\'' +
                '}';
    }
}
