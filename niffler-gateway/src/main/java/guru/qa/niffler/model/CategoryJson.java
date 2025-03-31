package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.model.gql.CategoryGqlInput;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CategoryJson(
    @JsonProperty("id")
    UUID id,
    @NotBlank(message = "Category can not be blank")
    @Size(min = 2, max = 50, message = "Allowed category length should be from 2 to 50 characters")
    @JsonProperty("name")
    String name,
    @JsonProperty("username")
    String username,
    @JsonProperty("archived")
    boolean archived) {

  public static CategoryJson fromCategoryInput(@Nonnull CategoryGqlInput input, @Nonnull String username) {
    return new CategoryJson(
        input.id(),
        input.name(),
        username,
        input.archived()
    );
  }
}
