package guru.qa.niffler.api.client;

import guru.qa.niffler.api.service.RestService;
import guru.qa.niffler.model.UserJson;

public class NifflerUserdataClient extends RestService {

  public NifflerUserdataClient() {
    super(CFG.getUserdataUrl());
  }

  private final NifflerUserdataApi userdataApi = retrofit.create(NifflerUserdataApi.class);

  public UserJson updateUser(UserJson userJson) throws Exception {
    return userdataApi.updateUserInfo(userJson)
        .execute()
        .body();
  }

  public UserJson getCurrentUser(String username) throws Exception {
    return userdataApi.currentUser(username)
        .execute()
        .body();
  }
}
