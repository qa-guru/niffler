package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.config.NifflerGatewayServiceConfig;
import guru.qa.niffler.userdata.wsdl.Currency;
import guru.qa.niffler.userdata.wsdl.User;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Size;

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
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("photo")
        @Size(max = NifflerGatewayServiceConfig.ONE_MB)
        String photo,
        @JsonProperty("photoSmall")
        String photoSmall,
        @JsonProperty("friendState")
        FriendState friendState) {

    public @Nonnull UserJson addUsername(@Nonnull String username) {
        return new UserJson(id, username, firstname, surname, currency, photo, photoSmall, friendState);
    }

    public @Nonnull User toJaxbUser() {
        User jaxbUser = new User();
        jaxbUser.setId(id != null ? id.toString() : null);
        jaxbUser.setUsername(username);
        jaxbUser.setFirstname(firstname);
        jaxbUser.setSurname(surname);
        jaxbUser.setCurrency(Currency.valueOf(currency.name()));
        jaxbUser.setPhoto(photo);
        jaxbUser.setPhotoSmall(photoSmall);
        jaxbUser.setFriendState(friendState() == null ?
                guru.qa.niffler.userdata.wsdl.FriendState.VOID :
                guru.qa.niffler.userdata.wsdl.FriendState.valueOf(friendState().name()));
        return jaxbUser;
    }

    public static @Nonnull UserJson fromJaxb(@Nonnull User jaxbUser) {
        return new UserJson(
                jaxbUser.getId() != null ? UUID.fromString(jaxbUser.getId()) : null,
                jaxbUser.getUsername(),
                jaxbUser.getFirstname(),
                jaxbUser.getSurname(),
                CurrencyValues.valueOf(jaxbUser.getCurrency().name()),
                jaxbUser.getPhoto(),
                jaxbUser.getPhotoSmall(),
                (jaxbUser.getFriendState() != null && jaxbUser.getFriendState() != guru.qa.niffler.userdata.wsdl.FriendState.VOID)
                        ? FriendState.valueOf(jaxbUser.getFriendState().name())
                        : null
        );
    }
}
