package guru.qa.niffler.model.graphql;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryInput(
        @NotBlank(message = "Category name can not be blank")
        @Size(max = 25, message = "Category can`t be longer than 50 characters")
        String category) {

}
