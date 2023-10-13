package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CategoryJson(
        @JsonProperty("id")
        UUID id,
        @NotEmpty(message = "Category can not be empty")
        @Size(max = 25, message = "Category can`t be longer than 50 characters")
        @JsonProperty("category")
        String category,
        @JsonProperty("username")
        String username) {

}
