package guru.qa.niffler.controller.graphql;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.SelectedField;
import guru.qa.niffler.config.NifflerGatewayServiceConfig;
import guru.qa.niffler.ex.TooManySubQueriesException;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.graphql.UpdateUserInfoInput;
import guru.qa.niffler.model.graphql.UserJsonGQL;
import guru.qa.niffler.service.UserDataClient;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = NifflerGatewayServiceConfig.OPEN_API_AUTH_SCHEME)
public class UserGraphqlController {

  private final UserDataClient userDataClient;

  @Autowired
  public UserGraphqlController(UserDataClient userDataClient) {
    this.userDataClient = userDataClient;
  }

  @SchemaMapping(typeName = "User", field = "friends")
  public List<UserJsonGQL> getFriends(UserJsonGQL user) {
    return userDataClient.friends(user.username(), null)
        .stream()
        .map(UserJsonGQL::fromUserJson)
        .toList();
  }

  @QueryMapping
  public UserJsonGQL user(@AuthenticationPrincipal Jwt principal,
                          @Nonnull DataFetchingEnvironment env) {
    checkSubQueries(env, 2, "friends", "invitations");
    String username = principal.getClaim("sub");
    UserJson userJson = userDataClient.currentUser(username);
    return UserJsonGQL.fromUserJson(userJson);
  }

  @QueryMapping
  public List<UserJsonGQL> users(@AuthenticationPrincipal Jwt principal) {
    String username = principal.getClaim("sub");
    return userDataClient.allUsers(username, null).stream()
        .map(UserJsonGQL::fromUserJson)
        .collect(Collectors.toList());
  }

  @MutationMapping
  public UserJsonGQL updateUser(@AuthenticationPrincipal Jwt principal,
                                @Argument @Valid UpdateUserInfoInput input) {
    String username = principal.getClaim("sub");
    return UserJsonGQL.fromUserJson(userDataClient.updateUserInfo(new UserJson(
        null,
        username,
        input.firstname(),
        input.surname(),
        input.fullname(),
        input.currency(),
        input.photo(),
        null,
        null
    )));
  }

  @MutationMapping
  public UserJsonGQL addFriend(@AuthenticationPrincipal Jwt principal,
                               @Argument String friendUsername) {
    String username = principal.getClaim("sub");
    return UserJsonGQL.fromUserJson(userDataClient.sendInvitation(username, friendUsername));
  }

  @MutationMapping
  public UserJsonGQL acceptInvitation(@AuthenticationPrincipal Jwt principal,
                                      @Argument String friendUsername) {
    String username = principal.getClaim("sub");
    return UserJsonGQL.fromUserJson(userDataClient.acceptInvitation(username, friendUsername));
  }

  @MutationMapping
  public UserJsonGQL declineInvitation(@AuthenticationPrincipal Jwt principal,
                                       @Argument String friendUsername) {
    String username = principal.getClaim("sub");
    return UserJsonGQL.fromUserJson(userDataClient.declineInvitation(username, friendUsername));
  }

  @MutationMapping
  public void removeFriend(@AuthenticationPrincipal Jwt principal,
                           @Argument String friendUsername) {
    String username = principal.getClaim("sub");
    userDataClient.removeFriend(username, friendUsername);
  }

  private void checkSubQueries(@Nonnull DataFetchingEnvironment env, int depth, @Nonnull String... queryKeys) {
    for (String queryKey : queryKeys) {
      List<SelectedField> selectors = env.getSelectionSet().getFieldsGroupedByResultKey().get(queryKey);
      if (selectors != null && selectors.size() > depth) {
        throw new TooManySubQueriesException("Can`t fetch over 2 " + queryKey + " sub-queries");
      }
    }
  }
}
