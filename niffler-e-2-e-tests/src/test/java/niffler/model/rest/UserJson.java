package niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class UserJson {
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
    @JsonProperty("password")
    private String password;
    @JsonProperty("friendState")
    private FriendState friendState;

    private transient List<CategoryJson> categoryJsons = new ArrayList<>();
    private transient List<SpendJson> spendJsons = new ArrayList<>();
    private transient List<UserJson> friendsJsons = new ArrayList<>();
    private transient List<UserJson> invitationsJsons = new ArrayList<>();

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<CategoryJson> getCategoryJsons() {
        return categoryJsons;
    }

    public void setCategoryJsons(List<CategoryJson> categoryJsons) {
        this.categoryJsons = categoryJsons;
    }

    public List<SpendJson> getSpendJsons() {
        return spendJsons;
    }

    public void setSpendJsons(List<SpendJson> spendJsons) {
        this.spendJsons = spendJsons;
    }

    public List<UserJson> getFriendsJsons() {
        return friendsJsons;
    }

    public void setFriendsJsons(List<UserJson> friendsJsons) {
        this.friendsJsons = friendsJsons;
    }

    public List<UserJson> getInvitationsJsons() {
        return invitationsJsons;
    }

    public void setInvitationsJsons(List<UserJson> invitationsJsons) {
        this.invitationsJsons = invitationsJsons;
    }

    public FriendState getFriendState() {
        return friendState;
    }

    public void setFriendState(FriendState friendState) {
        this.friendState = friendState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserJson userJson = (UserJson) o;
        return Objects.equals(id, userJson.id) && Objects.equals(username, userJson.username) && Objects.equals(firstname, userJson.firstname) && Objects.equals(surname, userJson.surname) && currency == userJson.currency && Objects.equals(photo, userJson.photo) && Objects.equals(password, userJson.password) && friendState == userJson.friendState && Objects.equals(categoryJsons, userJson.categoryJsons) && Objects.equals(spendJsons, userJson.spendJsons) && Objects.equals(friendsJsons, userJson.friendsJsons) && Objects.equals(invitationsJsons, userJson.invitationsJsons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, firstname, surname, currency, photo, password, friendState, categoryJsons, spendJsons, friendsJsons, invitationsJsons);
    }
}
