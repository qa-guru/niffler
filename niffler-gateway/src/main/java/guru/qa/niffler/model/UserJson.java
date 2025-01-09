package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.config.NifflerGatewayServiceConfig;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Size;
import jaxb.userdata.Currency;
import jaxb.userdata.User;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserJson(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("username")
    String username,
    @JsonProperty("firstname")
    @Size(max = 30, message = "First name can`t be longer than 30 characters")
    String firstname,
    @JsonProperty("surname")
    @Size(max = 50, message = "Surname can`t be longer than 50 characters")
    String surname,
    @JsonProperty("fullname")
    @Size(max = 100, message = "Fullname can`t be longer than 100 characters")
    String fullname,
    @JsonProperty("currency")
    CurrencyValues currency,
    @JsonProperty("photo")
    @Size(max = NifflerGatewayServiceConfig.ONE_MB)
    String photo,
    @JsonProperty("photoSmall")
    String photoSmall,
    @JsonProperty("friendshipStatus")
    FriendshipStatus friendshipStatus) {

  public @Nonnull UserJson addUsername(@Nonnull String username) {
    return new UserJson(id, username, firstname, surname, fullname, currency, photo, photoSmall, friendshipStatus);
  }

  public @Nonnull User toJaxbUser() {
    User jaxbUser = new User();
    jaxbUser.setId(id != null ? id.toString() : null);
    jaxbUser.setUsername(username);
    jaxbUser.setFirstname(firstname);
    jaxbUser.setSurname(surname);
    jaxbUser.setFullname(fullname);
    jaxbUser.setCurrency(currency != null ? Currency.valueOf(currency.name()) : null);
    jaxbUser.setPhoto(photo);
    jaxbUser.setPhotoSmall(photoSmall);
    jaxbUser.setFriendshipStatus(friendshipStatus() == null ?
        jaxb.userdata.FriendshipStatus.VOID :
        jaxb.userdata.FriendshipStatus.valueOf(friendshipStatus().name()));
    return jaxbUser;
  }

  public static @Nonnull UserJson fromJaxb(@Nonnull User jaxbUser) {
    return new UserJson(
        jaxbUser.getId() != null ? UUID.fromString(jaxbUser.getId()) : null,
        jaxbUser.getUsername(),
        jaxbUser.getFirstname(),
        jaxbUser.getSurname(),
        jaxbUser.getFullname(),
        jaxbUser.getCurrency() != null ? CurrencyValues.valueOf(jaxbUser.getCurrency().name()) : null,
        jaxbUser.getPhoto(),
        jaxbUser.getPhotoSmall(),
        (jaxbUser.getFriendshipStatus() != null && jaxbUser.getFriendshipStatus() != jaxb.userdata.FriendshipStatus.VOID)
            ? FriendshipStatus.valueOf(jaxbUser.getFriendshipStatus().name())
            : null
    );
  }
}
