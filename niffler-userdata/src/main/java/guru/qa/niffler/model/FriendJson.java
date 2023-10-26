package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import niffler_userdata.Friend;

public record FriendJson(
        @JsonProperty("username")
        String username) {

    @Nonnull
    public Friend toJaxbFriend() {
        Friend jaxbFriend = new Friend();
        jaxbFriend.setUsername(username());
        return jaxbFriend;
    }

    @Nonnull
    public static FriendJson fromJaxb(Friend jaxbFriend) {
        return new FriendJson(jaxbFriend.getUsername());
    }
}