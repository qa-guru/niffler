package guru.qa.niffler.controller.graphql;

import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.gql.FriendshipGqlInput;
import guru.qa.niffler.model.gql.UserGql;
import guru.qa.niffler.model.gql.UserGqlInput;
import guru.qa.niffler.service.UserDataClient;
import guru.qa.niffler.service.api.RestSpendClient;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

@Controller
@PreAuthorize("isAuthenticated()")
public class UserMutationController {

  private final UserDataClient userDataClient;
  private final RestSpendClient restSpendClient;

  @Autowired
  public UserMutationController(UserDataClient userDataClient, RestSpendClient restSpendClient) {
    this.userDataClient = userDataClient;
    this.restSpendClient = restSpendClient;
  }

  @MutationMapping
  public UserGql friendship(@AuthenticationPrincipal Jwt principal,
                            @Argument FriendshipGqlInput input) {
    String username = principal.getClaim("sub");
    return switch (input.action()) {
      case ADD -> UserGql.fromUserJson(userDataClient.sendInvitation(username, input.username()));
      case ACCEPT -> UserGql.fromUserJson(userDataClient.acceptInvitation(username, input.username()));
      case REJECT -> UserGql.fromUserJson(userDataClient.declineInvitation(username, input.username()));
      case DELETE -> {
        userDataClient.removeFriend(username, input.username());
        yield UserGql.fromUserJson(userDataClient.currentUser(username));
      }
    };
  }

  @MutationMapping
  public UserGql user(@AuthenticationPrincipal Jwt principal,
                      @Argument @Valid UserGqlInput input) {
    String username = principal.getClaim("sub");
    return UserGql.fromUserJson(userDataClient.updateUserInfo(new UserJson(
        null,
        username,
        null,
        null,
        input.fullname(),
        null,
        input.photo(),
        null,
        null
    )));
  }
}
