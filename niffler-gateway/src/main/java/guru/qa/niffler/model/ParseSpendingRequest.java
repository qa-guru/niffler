package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record ParseSpendingRequest(
    @JsonProperty("userInput")
    @NotBlank(message = "User input must not be blank")
    String userInput
) {
}

