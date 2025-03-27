package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.CurrencyValues;
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
    @JsonProperty("friendshipStatus")
    FriendshipStatus friendshipStatus) implements IUserJson {

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
    jaxbUser.setFriendshipStatus(friendshipStatus() == null ?
        jaxb.userdata.FriendshipStatus.VOID :
        jaxb.userdata.FriendshipStatus.valueOf(friendshipStatus().name()));
    return jaxbUser;
  }

  public static @Nonnull UserJsonBulk fromJaxb(@Nonnull User jaxbUser) {
    return new UserJsonBulk(
        jaxbUser.getId() != null ? UUID.fromString(jaxbUser.getId()) : null,
        jaxbUser.getUsername(),
        jaxbUser.getFullname(),
        CurrencyValues.valueOf(jaxbUser.getCurrency().name()),
        jaxbUser.getPhotoSmall(),
        (jaxbUser.getFriendshipStatus() != null && jaxbUser.getFriendshipStatus() != jaxb.userdata.FriendshipStatus.VOID)
            ? FriendshipStatus.valueOf(jaxbUser.getFriendshipStatus().name())
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
        projection.status() == guru.qa.niffler.data.FriendshipStatus.PENDING ? FriendshipStatus.INVITE_RECEIVED : FriendshipStatus.FRIEND
    );
  }

  public static @Nonnull UserJsonBulk fromUserEntityProjection(@Nonnull UserWithStatus projection) {
    return new UserJsonBulk(
        projection.id(),
        projection.username(),
        projection.fullname(),
        projection.currency(),
        projection.photoSmall() != null && projection.photoSmall().length > 0 ? new String(projection.photoSmall(), StandardCharsets.UTF_8) : null,
        projection.status() == guru.qa.niffler.data.FriendshipStatus.PENDING ? FriendshipStatus.INVITE_SENT : null
    );
  }
}
