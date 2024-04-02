package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FriendJson(
        @NotBlank(message = "Username can not be blank")
        @Size(max = 50, message = "Username can`t be longer than 50 characters")
        @JsonProperty("username")
        String username) {

}
