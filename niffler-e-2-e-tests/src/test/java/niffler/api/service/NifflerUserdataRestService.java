package niffler.api.service;

import niffler.api.NifflerUserdataApi;
import niffler.model.UserJson;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;

public class NifflerUserdataRestService extends RestService {

    public NifflerUserdataRestService() {
        super(CFG.userdataUrl());
    }

    private NifflerUserdataApi userdataApi = retrofit.create(NifflerUserdataApi.class);

    public UserJson updateUser(UserJson userJson) {
        UserJson response = null;
        try {
            response =  userdataApi.updateUserInfo(userJson).execute().body();
        } catch (IOException e) {
            Assertions.fail("### Could not update user", e);
        }
        return response;
    }
}
