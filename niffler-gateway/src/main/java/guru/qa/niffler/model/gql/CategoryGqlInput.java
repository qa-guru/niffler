package guru.qa.niffler.model.gql;

import guru.qa.niffler.validation.IsUuidString;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CategoryGqlInput(
    UUID id,
    @NotBlank(message = "Category can not be blank")
    @Size(min = 2, max = 50, message = "Allowed category length should be from 2 to 50 characters")
    String name,
    boolean archived
) {
}
