package niffler.data.model;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users", schema = "public", catalog = "niffler-userdata")
public class UsersEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "currency")
    private String currency;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "surname")
    private String surname;

    @Column(name = "photo")
    private byte[] photo;

    @OneToOne
    @JoinColumn( name = "friends_id")
    private FriendsEntity friendsEntity;


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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
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

    public FriendsEntity getFriendsEntity() {
        return friendsEntity;
    }

    public void setFriendsEntity(FriendsEntity friendsEntity) {
        this.friendsEntity = friendsEntity;
    }
}
