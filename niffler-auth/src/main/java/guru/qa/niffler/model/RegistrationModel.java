package guru.qa.niffler.model;

import guru.qa.niffler.validation.NoWhitespace;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@EqualPasswords
public record RegistrationModel(
    @NotBlank(message = "Username can not be blank")
    @NoWhitespace(message = "Username must not contain whitespaces")
    @Size(min = 3, max = 50, message = "Allowed username length should be from 3 to 50 characters")
    String username,
    @NotBlank(message = "Password can not be blank")
    @NoWhitespace(message = "Password must not contain whitespaces")
    @Size(min = 3, max = 12, message = "Allowed password length should be from 3 to 12 characters")
    String password,
    String passwordSubmit) {

}
