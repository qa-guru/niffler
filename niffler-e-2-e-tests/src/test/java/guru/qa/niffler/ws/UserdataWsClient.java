package guru.qa.niffler.ws;

import guru.qa.niffler.api.converter.JaxbConverterFactory;
import guru.qa.niffler.api.service.RestService;
import guru.qa.niffler.userdata.wsdl.AcceptInvitationRequest;
import guru.qa.niffler.userdata.wsdl.AcceptInvitationResponse;
import guru.qa.niffler.userdata.wsdl.AddFriendRequest;
import guru.qa.niffler.userdata.wsdl.AddFriendResponse;
import guru.qa.niffler.userdata.wsdl.AllUsersRequest;
import guru.qa.niffler.userdata.wsdl.AllUsersResponse;
import guru.qa.niffler.userdata.wsdl.CurrentUserRequest;
import guru.qa.niffler.userdata.wsdl.CurrentUserResponse;
import guru.qa.niffler.userdata.wsdl.DeclineInvitationRequest;
import guru.qa.niffler.userdata.wsdl.DeclineInvitationResponse;
import guru.qa.niffler.userdata.wsdl.FriendsRequest;
import guru.qa.niffler.userdata.wsdl.FriendsResponse;
import guru.qa.niffler.userdata.wsdl.InvitationsRequest;
import guru.qa.niffler.userdata.wsdl.InvitationsResponse;
import guru.qa.niffler.userdata.wsdl.RemoveFriendRequest;
import guru.qa.niffler.userdata.wsdl.RemoveFriendResponse;
import guru.qa.niffler.userdata.wsdl.UpdateUserInfoRequest;
import guru.qa.niffler.userdata.wsdl.UpdateUserInfoResponse;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UserdataWsClient extends RestService {

    public UserdataWsClient() {
        super(CFG.userdataUrl(), false, JaxbConverterFactory.create("niffler-userdata"));
    }

    private final UserdataWsApi userdataWs = retrofit.create(UserdataWsApi.class);

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: currentUserRequest")
    @Nullable
    public CurrentUserResponse currentUser(@Nonnull CurrentUserRequest currentUserRequest) throws Exception {
        return userdataWs.currentUserRequest(currentUserRequest)
                .execute()
                .body();
    }

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: updateUserInfoRequest")
    @Nullable
    public UpdateUserInfoResponse updateUserInfo(@Nonnull UpdateUserInfoRequest updateUserInfoRequest) throws Exception {
        return userdataWs.updateUserInfoRequest(updateUserInfoRequest)
                .execute()
                .body();
    }

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: allUsersRequest")
    @Nullable
    public AllUsersResponse allUsersRequest(@Nonnull AllUsersRequest allUsersRequest) throws Exception {
        return userdataWs.allUsersRequest(allUsersRequest)
                .execute()
                .body();
    }

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: friendsRequest")
    @Nullable
    public FriendsResponse friendsRequest(@Nonnull FriendsRequest friendsRequest) throws Exception {
        return userdataWs.friendsRequest(friendsRequest)
                .execute()
                .body();
    }

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: invitationsRequest")
    @Nullable
    public InvitationsResponse invitationsRequest(@Nonnull InvitationsRequest invitationsRequest) throws Exception {
        return userdataWs.invitationsRequest(invitationsRequest)
                .execute()
                .body();
    }

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: acceptInvitationRequest")
    @Nullable
    public AcceptInvitationResponse acceptInvitationRequest(@Nonnull AcceptInvitationRequest acceptInvitationRequest) throws Exception {
        return userdataWs.acceptInvitationRequest(acceptInvitationRequest)
                .execute()
                .body();
    }

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: declineInvitationRequest")
    @Nullable
    public DeclineInvitationResponse declineInvitationRequest(@Nonnull DeclineInvitationRequest declineInvitationRequest) throws Exception {
        return userdataWs.declineInvitationRequest(declineInvitationRequest)
                .execute()
                .body();
    }

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: addFriendRequest")
    @Nullable
    public AddFriendResponse addFriendRequest(@Nonnull AddFriendRequest addFriendRequest) throws Exception {
        return userdataWs.addFriendRequest(addFriendRequest)
                .execute()
                .body();
    }

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: removeFriendRequest")
    @Nullable
    public RemoveFriendResponse removeFriendRequest(@Nonnull RemoveFriendRequest removeFriendRequest) throws Exception {
        return userdataWs.removeFriendRequest(removeFriendRequest)
                .execute()
                .body();
    }
}
