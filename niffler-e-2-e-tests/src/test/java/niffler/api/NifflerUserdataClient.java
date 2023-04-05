package niffler.api;

import niffler.api.service.RestService;
import niffler.model.UserJson;

import java.util.List;

public class NifflerUserdataClient extends RestService {

    public NifflerUserdataClient() {
        super(CFG.userdataUrl());
    }

    private final NifflerUserdataApi userdataApi = retrofit.create(NifflerUserdataApi.class);

    public UserJson getCurrentUser(String username) throws Exception {
        return userdataApi.currentUser(username)
                .execute()
                .body();
    }

    public UserJson updateUser(UserJson userJson) throws Exception {
        return userdataApi.updateUserInfo(userJson)
                .execute()
                .body();
    }

    public List<UserJson> allUsers(String username) throws Exception {
        return userdataApi.allUsers(username)
                .execute()
                .body();
    }
 }
