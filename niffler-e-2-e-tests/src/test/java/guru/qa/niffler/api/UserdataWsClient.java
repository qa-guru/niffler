package guru.qa.niffler.api;

import guru.qa.niffler.api.converter.JaxbConverterFactory;
import guru.qa.niffler.api.service.RestClient;
import io.qameta.allure.Step;
import jaxb.userdata.AcceptInvitationRequest;
import jaxb.userdata.AllUsersPageRequest;
import jaxb.userdata.AllUsersRequest;
import jaxb.userdata.CurrentUserRequest;
import jaxb.userdata.DeclineInvitationRequest;
import jaxb.userdata.FriendsPageRequest;
import jaxb.userdata.FriendsRequest;
import jaxb.userdata.RemoveFriendRequest;
import jaxb.userdata.SendInvitationRequest;
import jaxb.userdata.UpdateUserRequest;
import jaxb.userdata.UserResponse;
import jaxb.userdata.UsersResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UserdataWsClient extends RestClient {

  private final UserdataWsApi userdataWs;

  public UserdataWsClient() {
    super(
        CFG.userdataUrl(),
        false,
        JaxbConverterFactory.create("niffler-userdata"),
        CFG.restLoggingLevel()
    );
    this.userdataWs = retrofit.create(UserdataWsApi.class);
  }

  @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: currentUserRequest")
  @Nullable
  public UserResponse currentUser(@Nonnull CurrentUserRequest currentUserRequest) throws Exception {
    return userdataWs.currentUserRequest(currentUserRequest)
        .execute()
        .body();
  }

  @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: updateUserRequest")
  @Nullable
  public UserResponse updateUserInfo(@Nonnull UpdateUserRequest updateUserRequest) throws Exception {
    return userdataWs.updateUserRequest(updateUserRequest)
        .execute()
        .body();
  }

  @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: allUsersRequest")
  @Nullable
  public UsersResponse allUsersRequest(@Nonnull AllUsersRequest allUsersRequest) throws Exception {
    return userdataWs.allUsersRequest(allUsersRequest)
        .execute()
        .body();
  }

  @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: allUsersPageRequest")
  @Nullable
  public UsersResponse allUsersPageRequest(@Nonnull AllUsersPageRequest allUsersPageRequest) throws Exception {
    return userdataWs.allUsersPageRequest(allUsersPageRequest)
        .execute()
        .body();
  }

  @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: friendsRequest")
  @Nullable
  public UsersResponse friendsRequest(@Nonnull FriendsRequest friendsRequest) throws Exception {
    return userdataWs.friendsRequest(friendsRequest)
        .execute()
        .body();
  }

  @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: friendsPageRequest")
  @Nullable
  public UsersResponse friendsPageRequest(@Nonnull FriendsPageRequest friendsPageRequest) throws Exception {
    return userdataWs.friendsPageRequest(friendsPageRequest)
        .execute()
        .body();
  }

  @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: removeFriendRequest")
  public void removeFriendRequest(@Nonnull RemoveFriendRequest removeFriendRequest) throws Exception {
    userdataWs.removeFriendRequest(removeFriendRequest)
        .execute();
  }

  @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: acceptInvitationRequest")
  @Nullable
  public UserResponse acceptInvitationRequest(@Nonnull AcceptInvitationRequest acceptInvitationRequest) throws Exception {
    return userdataWs.acceptInvitationRequest(acceptInvitationRequest)
        .execute()
        .body();
  }

  @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: declineInvitationRequest")
  @Nullable
  public UserResponse declineInvitationRequest(@Nonnull DeclineInvitationRequest declineInvitationRequest) throws Exception {
    return userdataWs.declineInvitationRequest(declineInvitationRequest)
        .execute()
        .body();
  }

  @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: sendInvitationRequest")
  @Nullable
  public UserResponse sendInvitationRequest(@Nonnull SendInvitationRequest sendInvitationRequest) throws Exception {
    return userdataWs.sendInvitationRequest(sendInvitationRequest)
        .execute()
        .body();
  }
}
