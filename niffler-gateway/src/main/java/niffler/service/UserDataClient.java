package niffler.service;

import jakarta.annotation.Nonnull;
import niffler.model.FriendJson;
import niffler.model.UserJson;

import java.util.List;

public interface UserDataClient {
    @Nonnull
    UserJson updateUserInfo(@Nonnull UserJson user);

    @Nonnull
    UserJson currentUser(@Nonnull String username);

    @Nonnull
    List<UserJson> allUsers(@Nonnull String username);

    @Nonnull
    List<UserJson> friends(@Nonnull String username, boolean includePending);

    @Nonnull
    List<UserJson> invitations(@Nonnull String username);

    @Nonnull
    List<UserJson> acceptInvitation(@Nonnull String username,
                                    @Nonnull FriendJson invitation);

    @Nonnull
    UserJson acceptInvitationAndReturnFriend(@Nonnull String username,
                                             @Nonnull FriendJson invitation);

    @Nonnull
    List<UserJson> declineInvitation(@Nonnull String username,
                                     @Nonnull FriendJson invitation);

    @Nonnull
    UserJson addFriend(@Nonnull String username,
                       @Nonnull FriendJson friend);

    @Nonnull
    List<UserJson> removeFriend(@Nonnull String username,
                                @Nonnull String friendUsername);
}
