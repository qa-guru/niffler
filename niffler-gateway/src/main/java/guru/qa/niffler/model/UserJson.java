package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.config.NifflerGatewayServiceConfig;
import guru.qa.niffler.model.graphql.UpdateUserInfoInput;
import guru.qa.niffler.userdata.wsdl.Currency;
import guru.qa.niffler.userdata.wsdl.User;
import jakarta.validation.constraints.Size;

import java.util.Objects;
import java.util.UUID;

public class UserJson {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("firstname")
    @Size(max = 30, message = "First name can`t be longer than 30 characters")
    private String firstname;
    @JsonProperty("surname")
    @Size(max = 50, message = "Surname can`t be longer than 50 characters")
    private String surname;
    @JsonProperty("currency")
    private CurrencyValues currency;
    @JsonProperty("photo")
    @Size(max = NifflerGatewayServiceConfig.THREE_MB)
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

    public static UserJson fromJaxb(User jaxbUser) {
        UserJson usr = new UserJson();
        usr.setPhoto(jaxbUser.getPhoto());
        usr.setId(jaxbUser.getId() != null ? UUID.fromString(jaxbUser.getId()) : null);
        usr.setUsername(jaxbUser.getUsername());
        usr.setFirstname(jaxbUser.getFirstname());
        usr.setSurname(jaxbUser.getSurname());
        usr.setCurrency(CurrencyValues.valueOf(jaxbUser.getCurrency().name()));
        return usr;
    }

    public User toJaxbUser() {
        User u = new User();
        u.setId(getId().toString());
        u.setUsername(getUsername());
        u.setFirstname(getFirstname());
        u.setSurname(getSurname());
        u.setCurrency(Currency.valueOf(getCurrency().name()));
        u.setPhoto(getPhoto());
        u.setFriendState(getFriendState() == null ?
                guru.qa.niffler.userdata.wsdl.FriendState.VOID :
                guru.qa.niffler.userdata.wsdl.FriendState.valueOf(getFriendState().name()));
        return u;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserJson userJson = (UserJson) o;
        return Objects.equals(id, userJson.id) && Objects.equals(username, userJson.username) && Objects.equals(firstname, userJson.firstname) && Objects.equals(surname, userJson.surname) && currency == userJson.currency && Objects.equals(photo, userJson.photo) && friendState == userJson.friendState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, firstname, surname, currency, photo, friendState);
    }

    public static UserJson fromUpdateUserInfoInput(UpdateUserInfoInput input) {
        UserJson userJson = new UserJson();
        userJson.setCurrency(input.getCurrency());
        userJson.setFirstname(input.getFirstname());
        userJson.setPhoto(input.getPhoto());
        userJson.setSurname(input.getSurname());
        return userJson;
    }
}
