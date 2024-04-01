package guru.qa.niffler.ws;

import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserDataService;
import niffler_userdata.FriendsRequest;
import niffler_userdata.RemoveFriendRequest;
import niffler_userdata.UsersResponse;
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
    public UsersResponse friendsRq(@RequestPayload FriendsRequest request) {
        UsersResponse response = new UsersResponse();
        List<UserJson> users = userService.friends(request.getUsername(), request.getSearchQuery());
        if (!users.isEmpty()) {
            response.getUser().addAll(
                    users.stream()
                            .map(UserJson::toJaxbUser)
                            .toList()
            );
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "removeFriendRequest")
    public void removeFriendRq(@RequestPayload RemoveFriendRequest request) {
        userService.removeFriend(
                request.getUsername(), request.getFriendToBeRemoved()
        );
    }
}
