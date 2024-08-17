package guru.qa.niffler.soap;

import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.UserJsonBulk;
import guru.qa.niffler.service.UserService;
import jaxb.userdata.AllUsersPageRequest;
import jaxb.userdata.AllUsersRequest;
import jaxb.userdata.CurrentUserRequest;
import jaxb.userdata.UpdateUserRequest;
import jaxb.userdata.UserResponse;
import jaxb.userdata.UsersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
public class UserEndpoint extends BaseEndpoint {

  private final UserService userService;

  @Autowired
  public UserEndpoint(UserService userService) {
    this.userService = userService;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateUserRequest")
  @ResponsePayload
  public UserResponse updateUserRq(@RequestPayload UpdateUserRequest request) {
    UserResponse response = new UserResponse();
    response.setUser(userService.update(UserJson.fromJaxb(request.getUser())).toJaxbUser());
    return response;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "currentUserRequest")
  @ResponsePayload
  public UserResponse currentUserRq(@RequestPayload CurrentUserRequest request) {
    UserResponse response = new UserResponse();
    response.setUser(userService.getCurrentUser(request.getUsername()).toJaxbUser());
    return response;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "allUsersRequest")
  @ResponsePayload
  public UsersResponse allUsersRq(@RequestPayload AllUsersRequest request) {
    UsersResponse response = new UsersResponse();
    List<UserJsonBulk> users = userService.allUsers(request.getUsername(), request.getSearchQuery());
    return enrichUsersResponse(users, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "allUsersPageRequest")
  @ResponsePayload
  public UsersResponse allUsersPageRq(@RequestPayload AllUsersPageRequest request) {
    UsersResponse response = new UsersResponse();
    Page<UserJsonBulk> users = userService.allUsers(
        request.getUsername(),
        new SpringPageable(request.getPageInfo()).pageable(),
        request.getSearchQuery()
    );
    return enrichUsersResponse(users, response);
  }
}
