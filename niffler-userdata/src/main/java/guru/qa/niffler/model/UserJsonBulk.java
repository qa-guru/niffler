package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.CurrencyValues;
import guru.qa.niffler.data.FriendshipStatus;
import guru.qa.niffler.data.projection.UserWithStatus;
import jakarta.annotation.Nonnull;
import jaxb.userdata.Currency;
import jaxb.userdata.User;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserJsonBulk(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("username")
    String username,
    @JsonProperty("fullname")
    String fullname,
    @JsonProperty("currency")
    CurrencyValues currency,
    @JsonProperty("photoSmall")
    String photoSmall,
    @JsonProperty("friendState")
    FriendState friendState) implements IUserJson {

  @Override
  public String photo() {
    return null;
  }

  @Override
  public String firstname() {
    return null;
  }

  @Override
  public String surname() {
    return null;
  }

  public @Nonnull User toJaxbUser() {
    User jaxbUser = new User();
    jaxbUser.setId(id != null ? id.toString() : null);
    jaxbUser.setUsername(username);
    jaxbUser.setFullname(fullname);
    jaxbUser.setCurrency(Currency.valueOf(currency.name()));
    jaxbUser.setPhotoSmall(photoSmall);
    jaxbUser.setFriendState(friendState() == null ?
        jaxb.userdata.FriendState.VOID :
        jaxb.userdata.FriendState.valueOf(friendState().name()));
    return jaxbUser;
  }

  public static @Nonnull UserJsonBulk fromJaxb(@Nonnull User jaxbUser) {
    return new UserJsonBulk(
        jaxbUser.getId() != null ? UUID.fromString(jaxbUser.getId()) : null,
        jaxbUser.getUsername(),
        jaxbUser.getFullname(),
        CurrencyValues.valueOf(jaxbUser.getCurrency().name()),
        jaxbUser.getPhotoSmall(),
        (jaxbUser.getFriendState() != null && jaxbUser.getFriendState() != jaxb.userdata.FriendState.VOID)
            ? FriendState.valueOf(jaxbUser.getFriendState().name())
            : null
    );
  }

  public static @Nonnull UserJsonBulk fromFriendEntityProjection(@Nonnull UserWithStatus projection) {
    return new UserJsonBulk(
        projection.id(),
        projection.username(),
        projection.fullname(),
        projection.currency(),
        projection.photoSmall() != null && projection.photoSmall().length > 0 ? new String(projection.photoSmall(), StandardCharsets.UTF_8) : null,
        projection.status() == FriendshipStatus.PENDING ? FriendState.INVITE_RECEIVED : FriendState.FRIEND
    );
  }

  public static @Nonnull UserJsonBulk fromUserEntityProjection(@Nonnull UserWithStatus projection) {
    return new UserJsonBulk(
        projection.id(),
        projection.username(),
        projection.fullname(),
        projection.currency(),
        projection.photoSmall() != null && projection.photoSmall().length > 0 ? new String(projection.photoSmall(), StandardCharsets.UTF_8) : null,
        projection.status() == FriendshipStatus.PENDING ? FriendState.INVITE_SENT : null
    );
  }
}
