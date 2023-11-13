package guru.qa.niffler.service.ws;

import guru.qa.niffler.ex.NoSoapResponseException;
import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserDataClient;
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
import jakarta.annotation.Nonnull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.util.List;
import java.util.Optional;

@Component
@ConditionalOnProperty(prefix = "niffler-userdata", name = "client", havingValue = "soap")
public class SoapUserDataClient extends WebServiceGatewaySupport implements UserDataClient {

    @Override
    public @Nonnull
    UserJson updateUserInfo(@Nonnull UserJson user) {
        UpdateUserInfoRequest request = new UpdateUserInfoRequest();
        request.setUser(user.toJaxbUser());

        UpdateUserInfoResponse response = Optional.ofNullable(
                (UpdateUserInfoResponse) getWebServiceTemplate().marshalSendAndReceive(
                        getDefaultUri(),
                        request
                )
        ).orElseThrow(() -> new NoSoapResponseException(
                "No SOAP UpdateUserInfoResponse is given [/updateUserInfo Endpoint]"
        ));

        return UserJson.fromJaxb(response.getUser());
    }

    @Override
    public @Nonnull
    UserJson currentUser(@Nonnull String username) {
        CurrentUserRequest request = new CurrentUserRequest();
        request.setUsername(username);

        CurrentUserResponse response = Optional.ofNullable(
                (CurrentUserResponse) getWebServiceTemplate().marshalSendAndReceive(
                        getDefaultUri(),
                        request
                )
        ).orElseThrow(() -> new NoSoapResponseException(
                "No SOAP CurrentUserResponse is given [/currentUser Endpoint]"
        ));

        return UserJson.fromJaxb(response.getUser());
    }

    @Override
    public @Nonnull
    List<UserJson> allUsers(@Nonnull String username) {
        AllUsersRequest request = new AllUsersRequest();
        request.setUsername(username);

        AllUsersResponse response = Optional.ofNullable(
                (AllUsersResponse) getWebServiceTemplate().marshalSendAndReceive(
                        getDefaultUri(),
                        request
                )
        ).orElseThrow(() -> new NoSoapResponseException(
                "No SOAP AllUsersResponse is given [/allUsers Endpoint]"
        ));

        return response.getUser().stream().map(UserJson::fromJaxb).toList();
    }

    @Override
    public @Nonnull
    List<UserJson> friends(@Nonnull String username, boolean includePending) {
        FriendsRequest request = new FriendsRequest();
        request.setUsername(username);
        request.setIncludePending(includePending);

        FriendsResponse response = Optional.ofNullable(
                (FriendsResponse) getWebServiceTemplate().marshalSendAndReceive(
                        getDefaultUri(),
                        request
                )
        ).orElseThrow(() -> new NoSoapResponseException(
                "No SOAP FriendsResponse is given [/friends Endpoint]"
        ));

        return response.getUser().stream().map(UserJson::fromJaxb).toList();
    }

    @Override
    public @Nonnull
    List<UserJson> invitations(@Nonnull String username) {
        InvitationsRequest request = new InvitationsRequest();
        request.setUsername(username);

        InvitationsResponse response = Optional.ofNullable(
                (InvitationsResponse) getWebServiceTemplate().marshalSendAndReceive(
                        getDefaultUri(),
                        request
                )
        ).orElseThrow(() -> new NoSoapResponseException(
                "No SOAP InvitationsResponse is given [/invitations Endpoint]"
        ));

        return response.getUser().stream().map(UserJson::fromJaxb).toList();
    }

    @Override
    public @Nonnull
    List<UserJson> acceptInvitation(@Nonnull String username,
                                    @Nonnull FriendJson invitation) {
        AcceptInvitationRequest request = new AcceptInvitationRequest();
        request.setUsername(username);
        request.setInvitation(invitation.toJaxbFriend());

        AcceptInvitationResponse response = Optional.ofNullable(
                (AcceptInvitationResponse) getWebServiceTemplate().marshalSendAndReceive(
                        getDefaultUri(),
                        request
                )
        ).orElseThrow(() -> new NoSoapResponseException(
                "No SOAP AcceptInvitationResponse is given [/acceptInvitation Endpoint]"
        ));

        return response.getUser().stream().map(UserJson::fromJaxb).toList();
    }

    @Override
    public @Nonnull UserJson acceptInvitationAndReturnFriend(@Nonnull String username, @Nonnull FriendJson invitation) {
        return acceptInvitation(username, invitation).stream()
                .filter(friend -> friend.username().equals(invitation.username()))
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

        DeclineInvitationResponse response = Optional.ofNullable(
                (DeclineInvitationResponse) getWebServiceTemplate().marshalSendAndReceive(
                        getDefaultUri(),
                        request
                )
        ).orElseThrow(() -> new NoSoapResponseException(
                "No SOAP DeclineInvitationResponse is given [/declineInvitation Endpoint]"
        ));

        return response.getUser().stream().map(UserJson::fromJaxb).toList();
    }

    @Override
    public @Nonnull UserJson addFriend(@Nonnull String username,
                                       @Nonnull FriendJson friend) {
        AddFriendRequest request = new AddFriendRequest();
        request.setUsername(username);
        request.setFriend(friend.toJaxbFriend());

        AddFriendResponse response = Optional.ofNullable(
                (AddFriendResponse) getWebServiceTemplate().marshalSendAndReceive(
                        getDefaultUri(),
                        request
                )
        ).orElseThrow(() -> new NoSoapResponseException(
                "No SOAP AddFriendResponse is given [/addFriend Endpoint]"
        ));

        return UserJson.fromJaxb(response.getUser());
    }

    @Override
    public @Nonnull
    List<UserJson> removeFriend(@Nonnull String username,
                                @Nonnull String friendUsername) {
        RemoveFriendRequest request = new RemoveFriendRequest();
        request.setUsername(username);
        request.setFriendUsername(friendUsername);

        RemoveFriendResponse response = Optional.ofNullable(
                (RemoveFriendResponse) getWebServiceTemplate().marshalSendAndReceive(
                        getDefaultUri(),
                        request
                )
        ).orElseThrow(() -> new NoSoapResponseException(
                "No SOAP RemoveFriendResponse is given [/removeFriend Endpoint]"
        ));

        return response.getUser().stream().map(UserJson::fromJaxb).toList();
    }
}
