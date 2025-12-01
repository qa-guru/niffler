package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FriendJson(
    @NotBlank(message = "Username can not be blank")
    @Size(min = 3, max = 50, message = "Allowed username length should be from 3 to 50 characters")
    @JsonProperty("username")
    String username) {

}
