package guru.qa.niffler.data.entity.ud;

import java.io.Serializable;
import java.util.UUID;

public class FriendsId implements Serializable {

    private UUID user;
    private UUID friend;

    public UUID getUser() {
        return user;
    }

    public void setUser(UUID user) {
        this.user = user;
    }

    public UUID getFriend() {
        return friend;
    }

    public void setFriend(UUID friend) {
        this.friend = friend;
    }
}
