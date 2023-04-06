package niffler.model.gql;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import niffler.model.rest.CurrencyValues;
import niffler.model.rest.FriendState;

import java.util.List;
import java.util.UUID;

public class UserGql {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("currency")
    private CurrencyValues currency;
    @JsonProperty("photo")
    private String photo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("friendState")
    private FriendState friendState;
    @JsonProperty("friends")
    private List<UserGql> friends;
    @JsonProperty("invitations")
    private List<UserGql> invitations;
    @JsonProperty("__typename")
    private String typeName;

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

    public CurrencyValues getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyValues currency) {
        this.currency = currency;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public FriendState getFriendState() {
        return friendState;
    }

    public void setFriendState(FriendState friendState) {
        this.friendState = friendState;
    }

    public List<UserGql> getFriends() {
        return friends;
    }

    public void setFriends(List<UserGql> friends) {
        this.friends = friends;
    }

    public List<UserGql> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<UserGql> invitations) {
        this.invitations = invitations;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
