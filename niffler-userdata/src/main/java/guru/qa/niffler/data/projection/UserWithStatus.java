package guru.qa.niffler.data.projection;

import guru.qa.niffler.data.CurrencyValues;
import guru.qa.niffler.data.FriendshipStatus;

import java.util.UUID;

public record UserWithStatus(
    UUID id,
    String username,
    CurrencyValues currency,
    String fullname,
    byte[] photoSmall,
    FriendshipStatus status
) {
}
