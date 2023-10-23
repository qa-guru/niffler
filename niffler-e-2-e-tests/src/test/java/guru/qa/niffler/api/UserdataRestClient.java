package guru.qa.niffler.api;

import guru.qa.niffler.api.service.RestService;
import guru.qa.niffler.model.rest.FriendJson;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class UserdataRestClient extends RestService {

    public UserdataRestClient() {
        super(CFG.userdataUrl());
    }

    private final UserdataApi userdataApi = retrofit.create(UserdataApi.class);

    @Step("Send REST GET('/currentUser') request to niffler-userdata")
    @Nullable
    public UserJson getCurrentUser(@Nonnull String username) throws Exception {
        return userdataApi.currentUser(username)
                .execute()
                .body();
    }

    @Step("Send REST POST('/updateUserInfo') request to niffler-userdata")
    @Nullable
    public UserJson updateUser(@Nonnull UserJson userJson) throws Exception {
        return userdataApi.updateUserInfo(userJson)
                .execute()
                .body();
    }

    @Step("Send REST GET('/allUsers') request to niffler-userdata")
    @Nullable
    public List<UserJson> allUsers(@Nonnull String username) throws Exception {
        return userdataApi.allUsers(username)
                .execute()
                .body();
    }

    @Step("Send REST GET('/friends') request to niffler-userdata")
    @Nullable
    public List<UserJson> friends(@Nonnull String username, boolean includePending) throws Exception {
        return userdataApi.friends(username, includePending)
                .execute()
                .body();
    }

    @Step("Send REST GET('/invitations') request to niffler-userdata")
    @Nullable
    public List<UserJson> invitations(@Nonnull String username) throws Exception {
        return userdataApi.invitations(username)
                .execute()
                .body();
    }

    @Step("Send REST POST('/acceptInvitation') request to niffler-userdata")
    @Nullable
    public List<UserJson> acceptInvitation(@Nonnull String username,
                                           @Nonnull FriendJson invitation) throws Exception {
        return userdataApi.acceptInvitation(username, invitation)
                .execute()
                .body();
    }

    @Step("Send REST POST('/declineInvitation') request to niffler-userdata")
    @Nullable
    public List<UserJson> declineInvitation(@Nonnull String username,
                                            @Nonnull FriendJson invitation) throws Exception {
        return userdataApi.declineInvitation(username, invitation)
                .execute()
                .body();
    }

    @Step("Send REST POST('/addFriend') request to niffler-userdata")
    @Nullable
    public UserJson addFriend(@Nonnull String username, @Nonnull FriendJson friend) throws Exception {
        return userdataApi.addFriend(username, friend)
                .execute()
                .body();
    }

    @Step("Send REST DELETE('/removeFriend') request to niffler-userdata")
    @Nullable
    public List<UserJson> removeFriend(@Nonnull String username, @Nonnull String friendUsername) throws Exception {
        return userdataApi.removeFriend(username, friendUsername)
                .execute()
                .body();
    }
}
