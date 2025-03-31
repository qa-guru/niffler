package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.validation.IsValidUsername;

public record FriendJson(
    @IsValidUsername
    @JsonProperty("username")
    String username) {

}
