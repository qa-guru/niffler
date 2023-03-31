package niffler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;

import java.util.Objects;
import java.util.UUID;

import static niffler.config.NifflerGatewayServiceConfig.THREE_MB;

public class UserJson {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("username")
    private String userName;
    @JsonProperty("firstname")
    @Size(max = 30, message = "First name can`t be longer than 30 characters")
    private String firstname;
    @JsonProperty("surname")
    @Size(max = 50, message = "Surname can`t be longer than 50 characters")
    private String surname;
    @JsonProperty("currency")
    private CurrencyValues currency;
    @JsonProperty("photo")
    @Size(max = THREE_MB)
    private String photo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("friendState")
    private FriendState friendState;

    public UserJson() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserJson userJson = (UserJson) o;
        return Objects.equals(id, userJson.id) && Objects.equals(userName, userJson.userName) && Objects.equals(firstname, userJson.firstname) && Objects.equals(surname, userJson.surname) && currency == userJson.currency && Objects.equals(photo, userJson.photo) && friendState == userJson.friendState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, firstname, surname, currency, photo, friendState);
    }
}
