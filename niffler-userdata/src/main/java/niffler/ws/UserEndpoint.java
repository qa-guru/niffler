package niffler.ws;

import niffler.model.UserJson;
import niffler.service.UserDataService;
import niffler_userdata.AllUsersRequest;
import niffler_userdata.AllUsersResponse;
import niffler_userdata.CurrentUserRequest;
import niffler_userdata.CurrentUserResponse;
import niffler_userdata.UpdateUserInfoRequest;
import niffler_userdata.UpdateUserInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
public class UserEndpoint {

    private static final String NAMESPACE_URI = "niffler-userdata";
    private final UserDataService userService;

    @Autowired
    public UserEndpoint(UserDataService userService) {
        this.userService = userService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateUserInfoRequest")
    @ResponsePayload
    public UpdateUserInfoResponse updateUserInfoRequest(@RequestPayload UpdateUserInfoRequest request) {
        UpdateUserInfoResponse response = new UpdateUserInfoResponse();
        response.setUser(userService.update(UserJson.fromJaxb(request.getUser())).toJaxbUser());
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "currentUserRequest")
    @ResponsePayload
    public CurrentUserResponse currentUserRequest(@RequestPayload CurrentUserRequest request) {
        CurrentUserResponse response = new CurrentUserResponse();
        response.setUser(userService.getCurrentUserOrCreateIfAbsent(request.getUsername()).toJaxbUser());
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "allUsersRequest")
    @ResponsePayload
    public AllUsersResponse allUsersRequest(@RequestPayload AllUsersRequest request) {
        AllUsersResponse response = new AllUsersResponse();
        List<UserJson> users = userService.allUsers(request.getUsername());
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
