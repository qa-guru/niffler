package guru.qa.niffler.api;

import guru.qa.niffler.api.converter.JaxbConverterFactory;
import guru.qa.niffler.api.service.RestClient;
import guru.qa.niffler.userdata.wsdl.AcceptInvitationRequest;
import guru.qa.niffler.userdata.wsdl.AllUsersRequest;
import guru.qa.niffler.userdata.wsdl.CurrentUserRequest;
import guru.qa.niffler.userdata.wsdl.DeclineInvitationRequest;
import guru.qa.niffler.userdata.wsdl.FriendsRequest;
import guru.qa.niffler.userdata.wsdl.IncomeInvitationsRequest;
import guru.qa.niffler.userdata.wsdl.OutcomeInvitationsRequest;
import guru.qa.niffler.userdata.wsdl.RemoveFriendRequest;
import guru.qa.niffler.userdata.wsdl.SendInvitationRequest;
import guru.qa.niffler.userdata.wsdl.UpdateUserRequest;
import guru.qa.niffler.userdata.wsdl.UserResponse;
import guru.qa.niffler.userdata.wsdl.UsersResponse;
import io.qameta.allure.Step;

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

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: friendsRequest")
    @Nullable
    public UsersResponse friendsRequest(@Nonnull FriendsRequest friendsRequest) throws Exception {
        return userdataWs.friendsRequest(friendsRequest)
                .execute()
                .body();
    }

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: removeFriendRequest")
    public void removeFriendRequest(@Nonnull RemoveFriendRequest removeFriendRequest) throws Exception {
        userdataWs.removeFriendRequest(removeFriendRequest)
                .execute();
    }

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: incomeInvitationsRequest")
    @Nullable
    public UsersResponse incomeInvitationsRequest(@Nonnull IncomeInvitationsRequest incomeInvitationsRequest) throws Exception {
        return userdataWs.incomeInvitationsRequest(incomeInvitationsRequest)
                .execute()
                .body();
    }

    @Step("Send SOAP POST('/ws') request to niffler-userdata, endpoint: outcomeInvitationsRequest")
    @Nullable
    public UsersResponse outcomeInvitationsRequest(@Nonnull OutcomeInvitationsRequest outcomeInvitationsRequest) throws Exception {
        return userdataWs.outcomeInvitationsRequest(outcomeInvitationsRequest)
                .execute()
                .body();
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
