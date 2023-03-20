package niffler.ws;

import niffler.model.FriendJson;
import niffler.model.UserJson;
import niffler.service.UserDataService;
import niffler_userdata.AcceptInvitationRequest;
import niffler_userdata.AcceptInvitationResponse;
import niffler_userdata.AddFriendRequest;
import niffler_userdata.AddFriendResponse;
import niffler_userdata.DeclineInvitationRequest;
import niffler_userdata.DeclineInvitationResponse;
import niffler_userdata.FriendsRequest;
import niffler_userdata.FriendsResponse;
import niffler_userdata.InvitationsRequest;
import niffler_userdata.InvitationsResponse;
import niffler_userdata.RemoveFriendRequest;
import niffler_userdata.RemoveFriendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
public class FriendsEndpoint {
    private static final String NAMESPACE_URI = "niffler-userdata";

    private final UserDataService userService;

    @Autowired
    public FriendsEndpoint(UserDataService userService) {
        this.userService = userService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "friendsRequest")
    @ResponsePayload
    public FriendsResponse friendsRequest(@RequestPayload FriendsRequest request) {
        FriendsResponse response = new FriendsResponse();
        List<UserJson> users = userService.friends(request.getUsername(), request.isIncludePending());
        if (!users.isEmpty()) {
            response.getUser().addAll(
                    users.stream()
                            .map(UserJson::toJaxbUser)
                            .toList()
            );
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "invitationsRequest")
    @ResponsePayload
    public InvitationsResponse invitationsRequest(@RequestPayload InvitationsRequest request) {
        InvitationsResponse response = new InvitationsResponse();
        List<UserJson> users = userService.invitations(request.getUsername());
        if (!users.isEmpty()) {
            response.getUser().addAll(
                    users.stream()
                            .map(UserJson::toJaxbUser)
                            .toList()
            );
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "acceptInvitationRequest")
    @ResponsePayload
    public AcceptInvitationResponse acceptInvitationRequest(@RequestPayload AcceptInvitationRequest request) {
        AcceptInvitationResponse response = new AcceptInvitationResponse();
        List<UserJson> users = userService.acceptInvitation(request.getUsername(), FriendJson.fromJaxb(request.getInvitation()));
        if (!users.isEmpty()) {
            response.getUser().addAll(
                    users.stream()
                            .map(UserJson::toJaxbUser)
                            .toList()
            );
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "declineInvitationRequest")
    @ResponsePayload
    public DeclineInvitationResponse declineInvitationRequest(@RequestPayload DeclineInvitationRequest request) {
        DeclineInvitationResponse response = new DeclineInvitationResponse();
        List<UserJson> users = userService.declineInvitation(request.getUsername(), FriendJson.fromJaxb(request.getInvitation()));
        if (!users.isEmpty()) {
            response.getUser().addAll(
                    users.stream()
                            .map(UserJson::toJaxbUser)
                            .toList()
            );
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addFriendRequest")
    @ResponsePayload
    public AddFriendResponse addFriendRequest(@RequestPayload AddFriendRequest request) {
        AddFriendResponse response = new AddFriendResponse();
        UserJson friend = userService.addFriend(request.getUsername(), FriendJson.fromJaxb(request.getFriend()));
        response.setUser(friend.toJaxbUser());
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "removeFriendRequest")
    @ResponsePayload
    public RemoveFriendResponse removeFriendRequest(@RequestPayload RemoveFriendRequest request) {
        RemoveFriendResponse response = new RemoveFriendResponse();
        List<UserJson> users = userService.removeFriend(request.getUsername(), request.getFriendUsername());
        if (!users.isEmpty()) {
            response.getUser().addAll(
                    users.stream()
                            .map(UserJson::toJaxbUser)
                            .toList()
            );
        }
        return response;
    }
}
