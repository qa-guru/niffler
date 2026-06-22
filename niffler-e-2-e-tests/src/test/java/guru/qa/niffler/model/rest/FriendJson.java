package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record FriendJson(
    @JsonProperty("username")
    String username) {

}
