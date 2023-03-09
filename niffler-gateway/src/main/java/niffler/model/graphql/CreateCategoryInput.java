package niffler.model.graphql;

public class CreateCategoryInput {
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
