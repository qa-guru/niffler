package guru.qa.niffler.ws;

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
import guru.qa.niffler.ws.service.SoapService;
import io.qameta.allure.Step;

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
