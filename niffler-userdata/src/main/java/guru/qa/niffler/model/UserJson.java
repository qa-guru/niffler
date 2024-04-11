package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.CurrencyValues;
import guru.qa.niffler.data.UserEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import niffler_userdata.Currency;
import niffler_userdata.User;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("username")
        String username,
        @JsonProperty("firstname")
        String firstname,
        @JsonProperty("surname")
        String surname,
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("photo")
        String photo,
        @JsonProperty("photoSmall")
        String photoSmall,
        @JsonProperty("friendState")
        FriendState friendState) implements IUserJson {

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
                niffler_userdata.FriendState.VOID :
                niffler_userdata.FriendState.valueOf(friendState().name()));
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
                (jaxbUser.getFriendState() != null && jaxbUser.getFriendState() != niffler_userdata.FriendState.VOID)
                        ? FriendState.valueOf(jaxbUser.getFriendState().name())
                        : null
        );
    }

    public static @Nonnull UserJson fromEntity(@Nonnull UserEntity entity, @Nullable FriendState friendState) {
        return new UserJson(
                entity.getId(),
                entity.getUsername(),
                entity.getFirstname(),
                entity.getSurname(),
                entity.getCurrency(),
                entity.getPhoto() != null && entity.getPhoto().length > 0 ? new String(entity.getPhoto(), StandardCharsets.UTF_8) : null,
                entity.getPhotoSmall() != null && entity.getPhotoSmall().length > 0 ? new String(entity.getPhotoSmall(), StandardCharsets.UTF_8) : null,
                friendState
        );
    }

    public static @Nonnull UserJson fromEntity(@Nonnull UserEntity entity) {
        return fromEntity(entity, null);
    }
}
