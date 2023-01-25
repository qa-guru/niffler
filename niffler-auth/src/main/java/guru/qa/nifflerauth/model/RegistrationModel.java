package guru.qa.nifflerauth.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@EqualPasswords
public class RegistrationModel {
    @NotNull(message = "Username can not be null")
    @NotEmpty(message = "Username can not be empty")
    @Size(max = 50, message = "Username can`t be longer than 50 characters")
    private String username;
    @NotNull(message = "Password can not be null")
    @Size(min = 3, max = 12, message = "Allowed password length should be from 3 to 12 characters")
    private String password;
    @NotNull(message = "Password submit can not be null")
    private String passwordSubmit;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordSubmit() {
        return passwordSubmit;
    }

    public void setPasswordSubmit(String passwordSubmit) {
        this.passwordSubmit = passwordSubmit;
    }
}
