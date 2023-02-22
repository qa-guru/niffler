package niffler.models.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class User {
    private String login;
    private String password;
    private boolean isAvailable;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        isAvailable = true;
    }

}
