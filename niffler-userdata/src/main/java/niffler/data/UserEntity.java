package niffler.data;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Entity
@Table(name = "users")
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

    @Column(nullable = true)
    private String firstname;

    @Column(nullable = true)
    private String surname;

    @Column(name = "photo", columnDefinition = "bytea")
    private byte[] photo;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendsEntity> friends = new ArrayList<>();

    @OneToMany(mappedBy = "friend", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendsEntity> invites = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public CurrencyValues getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyValues currency) {
        this.currency = currency;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public List<FriendsEntity> getFriends() {
        return friends;
    }

    public void setFriends(List<FriendsEntity> friends) {
        this.friends = friends;
    }

    public List<FriendsEntity> getInvites() {
        return invites;
    }

    public void setInvites(List<FriendsEntity> invites) {
        this.invites = invites;
    }

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

    public void removeFriends(UserEntity... friends) {
        for (UserEntity friend : friends) {
            getFriends().removeIf(f -> f.getFriend().getId().equals(friend.getId()));
        }
    }

    public void removeInvites(UserEntity... invitations) {
        for (UserEntity invite : invitations) {
            getInvites().removeIf(i -> i.getUser().getId().equals(invite.getId()));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && currency == that.currency && Objects.equals(firstname, that.firstname) && Objects.equals(surname, that.surname) && Arrays.equals(photo, that.photo) && Objects.equals(friends, that.friends) && Objects.equals(invites, that.invites);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, username, currency, firstname, surname, friends, invites);
        result = 31 * result + Arrays.hashCode(photo);
        return result;
    }
}
