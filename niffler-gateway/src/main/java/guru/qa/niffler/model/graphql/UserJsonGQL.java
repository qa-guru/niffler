package guru.qa.niffler.model.graphql;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.config.NifflerGatewayServiceConfig;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.FriendState;
import guru.qa.niffler.model.UserJson;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public class UserJsonGQL {
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
    @JsonProperty("friends")
    private List<UserJsonGQL> friends;
    @JsonProperty("invitations")
    private List<UserJsonGQL> invitations;

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

    public List<UserJsonGQL> getFriends() {
        return friends;
    }

    public void setFriends(List<UserJsonGQL> friends) {
        this.friends = friends;
    }

    public List<UserJsonGQL> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<UserJsonGQL> invitations) {
        this.invitations = invitations;
    }

    public static UserJsonGQL fromUserJson(UserJson userJson) {
        UserJsonGQL userGQLJson = new UserJsonGQL();
        userGQLJson.setId(userJson.getId());
        userGQLJson.setCurrency(userJson.getCurrency());
        userGQLJson.setFirstname(userJson.getFirstname());
        userGQLJson.setSurname(userJson.getSurname());
        userGQLJson.setUsername(userJson.getUsername());
        userGQLJson.setPhoto(userJson.getPhoto());
        userGQLJson.setFriendState(userJson.getFriendState());
        return userGQLJson;
    }
}
