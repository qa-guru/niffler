package niffler.api;

import io.qameta.allure.Step;
import niffler.api.service.RestService;
import niffler.model.rest.FriendJson;
import niffler.model.rest.UserJson;

import java.util.List;

public class NifflerUserdataClient extends RestService {

    public NifflerUserdataClient() {
        super(CFG.userdataUrl());
    }

    private final NifflerUserdataApi userdataApi = retrofit.create(NifflerUserdataApi.class);

    @Step("Send REST GET('/currentUser') request to niffler-userdata")
    public UserJson getCurrentUser(String username) throws Exception {
        return userdataApi.currentUser(username)
                .execute()
                .body();
    }

    @Step("Send REST POST('/updateUserInfo') request to niffler-userdata")
    public UserJson updateUser(UserJson userJson) throws Exception {
        return userdataApi.updateUserInfo(userJson)
                .execute()
                .body();
    }

    @Step("Send REST GET('/allUsers') request to niffler-userdata")
    public List<UserJson> allUsers(String username) throws Exception {
        return userdataApi.allUsers(username)
                .execute()
                .body();
    }

    @Step("Send REST POST('/addFriend') request to niffler-userdata")
    public UserJson addFriend(String username, FriendJson friend) throws Exception {
        return userdataApi.addFriend(username, friend)
                .execute()
                .body();
    }

    @Step("Send REST POST('/acceptInvitation') request to niffler-userdata")
    public List<UserJson> acceptInvitation(String username, FriendJson invitation) throws Exception {
        return userdataApi.acceptInvitation(username, invitation)
                .execute()
                .body();
    }
}
