package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import niffler_userdata.Friend;

public record FriendJson(
        @JsonProperty("username")
        String username) {

    public Friend toJaxbFriend() {
        Friend jaxbFriend = new Friend();
        jaxbFriend.setUsername(username());
        return jaxbFriend;
    }

    public static FriendJson fromJaxb(Friend jaxbFriend) {
        return new FriendJson(jaxbFriend.getUsername());
    }
}