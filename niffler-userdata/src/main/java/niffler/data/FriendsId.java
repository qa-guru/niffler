package niffler.data;

import java.io.Serializable;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendsId friendsId = (FriendsId) o;
        return Objects.equals(user, friendsId.user) && Objects.equals(friend, friendsId.friend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, friend);
    }
}
