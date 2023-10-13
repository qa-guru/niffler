package guru.qa.niffler.model.graphql;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateCategoryInput(
        @NotEmpty(message = "Category name can not be empty")
        @Size(max = 25, message = "Category can`t be longer than 50 characters")
        String category) {

}
