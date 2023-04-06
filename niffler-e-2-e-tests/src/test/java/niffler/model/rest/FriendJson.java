package niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FriendJson {

    @JsonProperty("username")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
