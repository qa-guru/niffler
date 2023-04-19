package niffler.ws;

import io.qameta.allure.Step;
import niffler.userdata.wsdl.AcceptInvitationRequest;
import niffler.userdata.wsdl.AcceptInvitationResponse;
import niffler.userdata.wsdl.AddFriendRequest;
import niffler.userdata.wsdl.AddFriendResponse;
import niffler.userdata.wsdl.AllUsersRequest;
import niffler.userdata.wsdl.AllUsersResponse;
import niffler.userdata.wsdl.CurrentUserRequest;
import niffler.userdata.wsdl.CurrentUserResponse;
import niffler.userdata.wsdl.DeclineInvitationRequest;
import niffler.userdata.wsdl.DeclineInvitationResponse;
import niffler.userdata.wsdl.FriendsRequest;
import niffler.userdata.wsdl.FriendsResponse;
import niffler.userdata.wsdl.InvitationsRequest;
import niffler.userdata.wsdl.InvitationsResponse;
import niffler.userdata.wsdl.RemoveFriendRequest;
import niffler.userdata.wsdl.RemoveFriendResponse;
import niffler.userdata.wsdl.UpdateUserInfoRequest;
import niffler.userdata.wsdl.UpdateUserInfoResponse;
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
