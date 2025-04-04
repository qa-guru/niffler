package guru.qa.niffler.api;

import guru.qa.niffler.api.service.RestClient;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.Step;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class UserdataApiClient extends RestClient {

  private final UserdataApi userdataApi;

  public UserdataApiClient() {
    super(CFG.userdataUrl());
    this.userdataApi = retrofit.create(UserdataApi.class);
  }

  @Step("Send REST GET('/internal/users/current') request to niffler-userdata")
  @Nullable
  public UserJson getCurrentUser(String username) throws Exception {
    return userdataApi.currentUser(username)
        .execute()
        .body();
  }

  @Step("Send REST POST('/internal/users/update') request to niffler-userdata")
  @Nullable
  public UserJson updateUser(UserJson userJson) throws Exception {
    return userdataApi.updateUserInfo(userJson)
        .execute()
        .body();
  }

  @Step("Send REST GET('/internal/users/all') request to niffler-userdata")
  @Nullable
  public List<UserJson> allUsers(String username,
                                 @Nullable String searchQuery) throws Exception {
    return userdataApi.allUsers(username, searchQuery)
        .execute()
        .body();
  }

  @Step("Send REST GET('/internal/friends/all') request to niffler-userdata")
  @Nullable
  public List<UserJson> friends(String username,
                                @Nullable String searchQuery) throws Exception {
    return userdataApi.friends(username, searchQuery)
        .execute()
        .body();
  }

  @Step("Send REST GET('/internal/invitations/income') request to niffler-userdata")
  @Nullable
  public List<UserJson> incomeInvitations(String username,
                                          @Nullable String searchQuery) throws Exception {
    return userdataApi.incomeInvitations(username, searchQuery)
        .execute()
        .body();
  }

  @Step("Send REST GET('/internal/invitations/outcome') request to niffler-userdata")
  @Nullable
  public List<UserJson> outcomeInvitations(String username,
                                           @Nullable String searchQuery) throws Exception {
    return userdataApi.outcomeInvitations(username, searchQuery)
        .execute()
        .body();
  }

  @Step("Send REST POST('/internal/invitations/accept') request to niffler-userdata")
  @Nullable
  public UserJson acceptInvitation(String username,
                                   String targetUsername) throws Exception {
    return userdataApi.acceptInvitation(username, targetUsername)
        .execute()
        .body();
  }

  @Step("Send REST POST('/internal/invitations/decline') request to niffler-userdata")
  @Nullable
  public UserJson declineInvitation(String username,
                                    String targetUsername) throws Exception {
    return userdataApi.declineInvitation(username, targetUsername)
        .execute()
        .body();
  }

  @Step("Send REST POST('/internal/invitations/send') request to niffler-userdata")
  @Nullable
  public UserJson sendInvitation(String username,
                                 String targetUsername) throws Exception {
    return userdataApi.sendInvitation(username, targetUsername)
        .execute()
        .body();
  }

  @Step("Send REST DELETE('/internal/friends/remove') request to niffler-userdata")
  public void removeFriend(String username,
                           String targetUsername) throws Exception {
    userdataApi.removeFriend(username, targetUsername)
        .execute();
  }
}
