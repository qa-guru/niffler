package guru.qa.niffler.data;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Getter
@Setter
@Entity
@Table(name = "\"user\"")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrencyValues currency;

    @Column()
    private String firstname;

    @Column()
    private String surname;

    @Column(name = "photo", columnDefinition = "bytea")
    private byte[] photo;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendsEntity> friends = new ArrayList<>();

    @OneToMany(mappedBy = "friend", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendsEntity> invites = new ArrayList<>();

    public void addFriends(boolean pending, UserEntity... friends) {
        List<FriendsEntity> friendsEntities = Stream.of(friends)
                .map(f -> {
                    FriendsEntity fe = new FriendsEntity();
                    fe.setUser(this);
                    fe.setFriend(f);
                    fe.setPending(pending);
                    return fe;
                }).toList();
        this.friends.addAll(friendsEntities);
    }

    public void addInvitations(UserEntity... invitations) {
        List<FriendsEntity> invitationsEntities = Stream.of(invitations)
                .map(i -> {
                    FriendsEntity fe = new FriendsEntity();
                    fe.setUser(i);
                    fe.setFriend(this);
                    fe.setPending(true);
                    return fe;
                }).toList();
        this.invites.addAll(invitationsEntities);
    }

    public void removeFriends(UserEntity... friends) {
        List<UUID> idsToBeRemoved = Arrays.stream(friends).map(UserEntity::getId).toList();
        for (Iterator<FriendsEntity> i = getFriends().iterator(); i.hasNext(); ) {
            FriendsEntity friendsEntity = i.next();
            if (idsToBeRemoved.contains(friendsEntity.getFriend().getId())) {
                friendsEntity.setFriend(null);
                i.remove();
            }
        }
    }

    public void removeInvites(UserEntity... invitations) {
        List<UUID> idsToBeRemoved = Arrays.stream(invitations).map(UserEntity::getId).toList();
        for (Iterator<FriendsEntity> i = getInvites().iterator(); i.hasNext(); ) {
            FriendsEntity friendsEntity = i.next();
            if (idsToBeRemoved.contains(friendsEntity.getUser().getId())) {
                friendsEntity.setUser(null);
                i.remove();
            }
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UserEntity that = (UserEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}