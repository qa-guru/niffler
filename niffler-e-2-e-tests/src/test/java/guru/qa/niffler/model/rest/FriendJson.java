package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FriendJson(
    @JsonProperty("username")
    String username) {

}
