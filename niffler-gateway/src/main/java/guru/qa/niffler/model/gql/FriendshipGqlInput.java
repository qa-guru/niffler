package guru.qa.niffler.model.gql;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FriendshipGqlInput(
    @NotBlank(message = "Username can not be blank")
    @Size(min = 3, max = 50, message = "Allowed username length should be from 3 to 50 characters")
    String username,
    FriendshipAction action) {
}
