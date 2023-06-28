package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserJson {
    @JsonProperty("username")
    private String username;

    public UserJson() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
