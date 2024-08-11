package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.CategoryEntity;
import jakarta.annotation.Nonnull;

import java.util.UUID;

public record CategoryJson(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("name")
    String name,
    @JsonProperty("username")
    String username,
    @JsonProperty("archived")
    boolean archived) {

  public static @Nonnull CategoryJson fromEntity(@Nonnull CategoryEntity entity) {
    return new CategoryJson(
        entity.getId(),
        entity.getName(),
        entity.getUsername(),
        entity.isArchived()
    );
  }
}
