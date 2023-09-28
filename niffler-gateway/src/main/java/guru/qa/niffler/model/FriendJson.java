package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.userdata.wsdl.Friend;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import javax.annotation.Nonnull;

public record FriendJson(
        @NotNull(message = "Username can not be null")
        @NotEmpty(message = "Username can not be empty")
        @Size(max = 50, message = "Username can`t be longer than 50 characters")
        @JsonProperty("username")
        String username) {

    public static @Nonnull FriendJson fromJaxb(@Nonnull Friend jaxbFriend) {
        return new FriendJson(jaxbFriend.getUsername());
    }

    public @Nonnull Friend toJaxbFriend() {
        Friend jaxbFriend = new Friend();
        jaxbFriend.setUsername(username);
        return jaxbFriend;
    }
}
