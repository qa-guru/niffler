package niffler.service.ws;

import jakarta.annotation.Nonnull;
import niffler.model.FriendJson;
import niffler.model.UserJson;
import niffler.service.UserDataClient;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.util.List;

@Component
@Qualifier("soap")
public class SoapUserDataClient extends WebServiceGatewaySupport implements UserDataClient {

    @Override
    public @Nonnull
    UserJson updateUserInfo(@Nonnull UserJson user) {
        UpdateUserInfoRequest request = new UpdateUserInfoRequest();
        request.setUser(user.toJaxbUser());

        UpdateUserInfoResponse response = (UpdateUserInfoResponse) getWebServiceTemplate()
                .marshalSendAndReceive(getDefaultUri(), request);

        return UserJson.fromJaxb(response.getUser());
    }

    @Override
    public @Nonnull
    UserJson currentUser(@Nonnull String username) {
        CurrentUserRequest request = new CurrentUserRequest();
        request.setUsername(username);

        CurrentUserResponse response = (CurrentUserResponse) getWebServiceTemplate()
                .marshalSendAndReceive(getDefaultUri(), request);

        return UserJson.fromJaxb(response.getUser());
    }

    @Override
    public @Nonnull
    List<UserJson> allUsers(@Nonnull String username) {
        AllUsersRequest request = new AllUsersRequest();
        request.setUsername(username);

        AllUsersResponse response = (AllUsersResponse) getWebServiceTemplate()
                .marshalSendAndReceive(getDefaultUri(), request);

        return response.getUser().stream().map(UserJson::fromJaxb).toList();
    }

    @Override
    public @Nonnull
    List<UserJson> friends(@Nonnull String username, boolean includePending) {
        FriendsRequest request = new FriendsRequest();
        request.setUsername(username);
        request.setIncludePending(includePending);

        FriendsResponse response = (FriendsResponse) getWebServiceTemplate()
                .marshalSendAndReceive(getDefaultUri(), request);

        return response.getUser().stream().map(UserJson::fromJaxb).toList();
    }

    @Override
    public @Nonnull
    List<UserJson> invitations(@Nonnull String username) {
        InvitationsRequest request = new InvitationsRequest();
        request.setUsername(username);

        InvitationsResponse response = (InvitationsResponse) getWebServiceTemplate()
                .marshalSendAndReceive(getDefaultUri(), request);

        return response.getUser().stream().map(UserJson::fromJaxb).toList();
    }

    @Override
    public @Nonnull
    List<UserJson> acceptInvitation(@Nonnull String username,
                                    @Nonnull FriendJson invitation) {
        AcceptInvitationRequest request = new AcceptInvitationRequest();
        request.setUsername(username);
        request.setInvitation(invitation.toJaxbFriend());

        AcceptInvitationResponse response = (AcceptInvitationResponse) getWebServiceTemplate()
                .marshalSendAndReceive(getDefaultUri(), request);

        return response.getUser().stream().map(UserJson::fromJaxb).toList();
    }

    @Override
    public UserJson acceptInvitationAndReturnFriend(String username, FriendJson invitation) {
        return acceptInvitation(username, invitation).stream()
                .filter(friend -> friend.getUsername().equals(invitation.getUsername()))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public @Nonnull
    List<UserJson> declineInvitation(@Nonnull String username,
                                     @Nonnull FriendJson invitation) {
        DeclineInvitationRequest request = new DeclineInvitationRequest();
        request.setUsername(username);
        request.setInvitation(invitation.toJaxbFriend());

        DeclineInvitationResponse response = (DeclineInvitationResponse) getWebServiceTemplate()
                .marshalSendAndReceive(getDefaultUri(), request);

        return response.getUser().stream().map(UserJson::fromJaxb).toList();
    }

    @Override
    public UserJson addFriend(@Nonnull String username,
                              @Nonnull FriendJson friend) {
        AddFriendRequest request = new AddFriendRequest();
        request.setUsername(username);
        request.setFriend(friend.toJaxbFriend());

        AddFriendResponse response = (AddFriendResponse) getWebServiceTemplate()
                .marshalSendAndReceive(getDefaultUri(), request);
        return UserJson.fromJaxb(response.getUser());
    }

    @Override
    public @Nonnull
    List<UserJson> removeFriend(@Nonnull String username,
                                @Nonnull String friendUsername) {
        RemoveFriendRequest request = new RemoveFriendRequest();
        request.setUsername(username);
        request.setFriendUsername(friendUsername);

        RemoveFriendResponse response = (RemoveFriendResponse) getWebServiceTemplate()
                .marshalSendAndReceive(getDefaultUri(), request);

        return response.getUser().stream().map(UserJson::fromJaxb).toList();
    }
}
