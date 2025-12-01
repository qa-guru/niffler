package guru.qa.niffler.controller.graphql;

import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.gql.FriendshipGqlInput;
import guru.qa.niffler.model.gql.UserGql;
import guru.qa.niffler.model.gql.UserGqlInput;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.UserDataClient;
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
  private final SpendClient spendClient;

  @Autowired
  public UserMutationController(UserDataClient userDataClient, SpendClient spendClient) {
    this.userDataClient = userDataClient;
    this.spendClient = spendClient;
  }

  @MutationMapping
  public UserGql friendship(@AuthenticationPrincipal Jwt principal,
                            @Valid @Argument FriendshipGqlInput input) {
    final String principalUsername = principal.getClaim("sub");
    return switch (input.action()) {
      case ADD -> UserGql.fromUserJson(userDataClient.sendInvitation(principalUsername, input.username()));
      case ACCEPT -> UserGql.fromUserJson(userDataClient.acceptInvitation(principalUsername, input.username()));
      case REJECT -> UserGql.fromUserJson(userDataClient.declineInvitation(principalUsername, input.username()));
      case DELETE -> {
        userDataClient.removeFriend(principalUsername, input.username());
        yield UserGql.fromUserJson(userDataClient.currentUser(principalUsername));
      }
    };
  }

  @MutationMapping
  public UserGql user(@AuthenticationPrincipal Jwt principal,
                      @Argument @Valid UserGqlInput input) {
    final String principalUsername = principal.getClaim("sub");
    return UserGql.fromUserJson(userDataClient.updateUserInfo(new UserJson(
        null,
        principalUsername,
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
