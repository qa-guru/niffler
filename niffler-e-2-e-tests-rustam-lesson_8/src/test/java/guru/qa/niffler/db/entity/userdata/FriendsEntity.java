package guru.qa.niffler.db.entity.userdata;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "friends")
@IdClass(FriendsId.class)
public class FriendsEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserDataEntity user;

    @Id
    @ManyToOne
    @JoinColumn(name = "friend_id", referencedColumnName = "id")
    private UserDataEntity friend;

    @Column(name = "pending")
    private boolean pending;

    public UserDataEntity getUser() {
        return user;
    }

    public void setUser(UserDataEntity user) {
        this.user = user;
    }

    public UserDataEntity getFriend() {
        return friend;
    }

    public void setFriend(UserDataEntity friend) {
        this.friend = friend;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendsEntity that = (FriendsEntity) o;
        return pending == that.pending && Objects.equals(user, that.user) && Objects.equals(friend, that.friend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, friend, pending);
    }
}
