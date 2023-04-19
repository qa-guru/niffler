package niffler.ws;

import io.qameta.allure.Step;
import niffler.model.soap.AcceptInvitationRequest;
import niffler.model.soap.AcceptInvitationResponse;
import niffler.model.soap.AddFriendRequest;
import niffler.model.soap.AddFriendResponse;
import niffler.model.soap.AllUsersRequest;
import niffler.model.soap.AllUsersResponse;
import niffler.model.soap.CurrentUserRequest;
import niffler.model.soap.CurrentUserResponse;
import niffler.model.soap.DeclineInvitationRequest;
import niffler.model.soap.DeclineInvitationResponse;
import niffler.model.soap.FriendsRequest;
import niffler.model.soap.FriendsResponse;
import niffler.model.soap.InvitationsRequest;
import niffler.model.soap.InvitationsResponse;
import niffler.model.soap.RemoveFriendRequest;
import niffler.model.soap.RemoveFriendResponse;
import niffler.model.soap.UpdateUserInfoRequest;
import niffler.model.soap.UpdateUserInfoResponse;
import niffler.ws.service.SoapService;

public class NifflerUserdataWsService extends SoapService {
    public NifflerUserdataWsService() {
        super(CFG.userdataUrl());
    }

    private final NifflerUserdataWs userdataWs = retrofit.create(NifflerUserdataWs.class);

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: currentUserRequest")
    public CurrentUserResponse currentUser(CurrentUserRequest currentUserRequest) throws Exception {
        return userdataWs.currentUserRequest(currentUserRequest)
                .execute()
                .body();
    }

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: updateUserInfoRequest")
    public UpdateUserInfoResponse updateUserInfo(UpdateUserInfoRequest updateUserInfoRequest) throws Exception {
        return userdataWs.updateUserInfoRequest(updateUserInfoRequest)
                .execute()
                .body();
    }

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: allUsersRequest")
    public AllUsersResponse allUsersRequest(AllUsersRequest allUsersRequest) throws Exception {
        return userdataWs.allUsersRequest(allUsersRequest)
                .execute()
                .body();
    }

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: friendsRequest")
    public FriendsResponse friendsRequest(FriendsRequest friendsRequest) throws Exception {
        return userdataWs.friendsRequest(friendsRequest)
                .execute()
                .body();
    }

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: invitationsRequest")
    public InvitationsResponse invitationsRequest(InvitationsRequest invitationsRequest) throws Exception {
        return userdataWs.invitationsRequest(invitationsRequest)
                .execute()
                .body();
    }

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: acceptInvitationRequest")
    public AcceptInvitationResponse acceptInvitationRequest(AcceptInvitationRequest acceptInvitationRequest) throws Exception {
        return userdataWs.acceptInvitationRequest(acceptInvitationRequest)
                .execute()
                .body();
    }

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: declineInvitationRequest")
    public DeclineInvitationResponse declineInvitationRequest(DeclineInvitationRequest declineInvitationRequest) throws Exception {
        return userdataWs.declineInvitationRequest(declineInvitationRequest)
                .execute()
                .body();
    }

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: addFriendRequest")
    public AddFriendResponse addFriendRequest(AddFriendRequest addFriendRequest) throws Exception {
        return userdataWs.addFriendRequest(addFriendRequest)
                .execute()
                .body();
    }

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: removeFriendRequest")
    public RemoveFriendResponse removeFriendRequest(RemoveFriendRequest removeFriendRequest) throws Exception {
        return userdataWs.removeFriendRequest(removeFriendRequest)
                .execute()
                .body();
    }
}
