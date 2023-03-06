package niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class FriendJson {
    @NotNull(message = "Username can not be null")
    @NotEmpty(message = "Username can not be empty")
    @Size(max = 50, message = "Username can`t be longer than 50 characters")
    @JsonProperty("username")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
