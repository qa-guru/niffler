package niffler.service.ws;

import jakarta.annotation.Nonnull;
import niffler.model.FriendJson;
import niffler.model.UserJson;
import niffler.userdata.wsdl.AcceptInvitationRequest;
import niffler.userdata.wsdl.AcceptInvitationResponse;
import niffler.userdata.wsdl.AddFriendRequest;
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
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.util.List;

public class SoapUserDataClient extends WebServiceGatewaySupport {

    public @Nonnull
    UserJson updateUserInfo(@Nonnull UserJson user) {
        UpdateUserInfoRequest request = new UpdateUserInfoRequest();
        request.setUser(user.toJaxbUser());

        UpdateUserInfoResponse response = (UpdateUserInfoResponse) getWebServiceTemplate()
                .marshalSendAndReceive(getDefaultUri() + "/userdata", request);

        return UserJson.fromJaxb(response.getUser());
    }

    public @Nonnull
    UserJson currentUser(@Nonnull String username) {
        CurrentUserRequest request = new CurrentUserRequest();
        request.setUsername(username);

        CurrentUserResponse response = (CurrentUserResponse) getWebServiceTemplate()
                .marshalSendAndReceive(getDefaultUri() + "/userdata", request);

        return UserJson.fromJaxb(response.getUser());
    }

    public @Nonnull
    List<UserJson> allUsers(@Nonnull String username) {
        AllUsersRequest request = new AllUsersRequest();
        request.setUsername(username);

        AllUsersResponse response = (AllUsersResponse) getWebServiceTemplate()
                .marshalSendAndReceive(getDefaultUri() + "/userdata", request);

        return response.getUser().stream().map(UserJson::fromJaxb).toList();
    }

    public @Nonnull
    List<UserJson> friends(@Nonnull String username, boolean includePending) {
        FriendsRequest request = new FriendsRequest();
        request.setUsername(username);
        request.setIncludePending(includePending);

        FriendsResponse response = (FriendsResponse) getWebServiceTemplate()
                .marshalSendAndReceive(getDefaultUri() + "/userdata", request);

        return response.getUser().stream().map(UserJson::fromJaxb).toList();
    }

    public @Nonnull
    List<UserJson> invitations(@Nonnull String username) {
        InvitationsRequest request = new InvitationsRequest();
        request.setUsername(username);

        InvitationsResponse response = (InvitationsResponse) getWebServiceTemplate()
                .marshalSendAndReceive(getDefaultUri() + "/userdata", request);

        return response.getUser().stream().map(UserJson::fromJaxb).toList();
    }

    public @Nonnull
    List<UserJson> acceptInvitation(@Nonnull String username,
                                    @Nonnull FriendJson invitation) {
        AcceptInvitationRequest request = new AcceptInvitationRequest();
        request.setUsername(username);
        request.setInvitation(invitation.toJaxbFriend());

        AcceptInvitationResponse response = (AcceptInvitationResponse) getWebServiceTemplate()
                .marshalSendAndReceive(getDefaultUri() + "/userdata", request);

        return response.getUser().stream().map(UserJson::fromJaxb).toList();
    }

    public @Nonnull
    List<UserJson> declineInvitation(@Nonnull String username,
                                     @Nonnull FriendJson invitation) {
        DeclineInvitationRequest request = new DeclineInvitationRequest();
        request.setUsername(username);
        request.setInvitation(invitation.toJaxbFriend());

        DeclineInvitationResponse response = (DeclineInvitationResponse) getWebServiceTemplate()
                .marshalSendAndReceive(getDefaultUri() + "/userdata", request);

        return response.getUser().stream().map(UserJson::fromJaxb).toList();
    }

    public void addFriend(@Nonnull String username,
                          @Nonnull FriendJson friend) {
        AddFriendRequest request = new AddFriendRequest();
        request.setUsername(username);
        request.setFriend(friend.toJaxbFriend());

        getWebServiceTemplate()
                .marshalSendAndReceive(getDefaultUri() + "/userdata", request);
    }

    public @Nonnull
    List<UserJson> removeFriend(@Nonnull String username,
                                @Nonnull String friendUsername) {
        RemoveFriendRequest request = new RemoveFriendRequest();
        request.setUsername(username);
        request.setFriendUsername(friendUsername);

        RemoveFriendResponse response = (RemoveFriendResponse) getWebServiceTemplate()
                .marshalSendAndReceive(getDefaultUri() + "/userdata", request);

        return response.getUser().stream().map(UserJson::fromJaxb).toList();
    }
}
