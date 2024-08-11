package guru.qa.niffler.soap;

import guru.qa.niffler.service.UserDataService;
import niffler_userdata.AcceptInvitationRequest;
import niffler_userdata.DeclineInvitationRequest;
import niffler_userdata.SendInvitationRequest;
import niffler_userdata.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class InvitationsEndpoint extends BaseEndpoint {

  private final UserDataService userService;

  @Autowired
  public InvitationsEndpoint(UserDataService userService) {
    this.userService = userService;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "sendInvitationRequest")
  @ResponsePayload
  public UserResponse sendInvitationRq(@RequestPayload SendInvitationRequest request) {
    UserResponse response = new UserResponse();
    response.setUser(
        userService.createFriendshipRequest(
            request.getUsername(), request.getFriendToBeRequested()
        ).toJaxbUser()
    );
    return response;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "acceptInvitationRequest")
  @ResponsePayload
  public UserResponse acceptInvitationRq(@RequestPayload AcceptInvitationRequest request) {
    UserResponse response = new UserResponse();
    response.setUser(
        userService.acceptFriendshipRequest(
            request.getUsername(), request.getFriendToBeAdded()
        ).toJaxbUser()
    );
    return response;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "declineInvitationRequest")
  @ResponsePayload
  public UserResponse declineInvitationRq(@RequestPayload DeclineInvitationRequest request) {
    UserResponse response = new UserResponse();
    response.setUser(
        userService.declineFriendshipRequest(
            request.getUsername(), request.getInvitationToBeDeclined()
        ).toJaxbUser()
    );
    return response;
  }
}
