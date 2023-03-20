package niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import niffler_userdata.Friend;

public class FriendJson {

    @JsonProperty("username")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static FriendJson fromJaxb(Friend jaxbFriend) {
        FriendJson friend = new FriendJson();
        friend.setUsername(jaxbFriend.getUsername());
        return friend;
    }

    public Friend toJaxbFriend() {
        Friend f = new Friend();
        f.setUsername(getUsername());
        return f;
    }
}
