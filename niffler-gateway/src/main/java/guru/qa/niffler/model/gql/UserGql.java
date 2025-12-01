package guru.qa.niffler.model.gql;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.FriendshipStatus;
import guru.qa.niffler.model.UserJson;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.UUID;

public record UserGql(
    UUID id,
    String username,
    String fullname,
    CurrencyValues currency,
    String photo,
    String photoSmall,
    FriendshipStatus friendshipStatus,
    List<CategoryJson> categories,
    Slice<UserGql> friends,
    Slice<UserGql> allPeople) {

  public static UserGql fromUserJson(UserJson userJson) {
    return new UserGql(
        userJson.id(),
        userJson.username(),
        userJson.fullname(),
        userJson.currency(),
        userJson.photo(),
        userJson.photoSmall(),
        userJson.friendshipStatus(),
        null,
        null,
        null
    );
  }
}
