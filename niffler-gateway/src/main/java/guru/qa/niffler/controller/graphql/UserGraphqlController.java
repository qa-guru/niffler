package guru.qa.niffler.controller.graphql;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.SelectedField;
import guru.qa.niffler.ex.ToManySubQueriesException;
import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.graphql.UpdateUserInfoInput;
import guru.qa.niffler.model.graphql.UserJsonGQL;
import guru.qa.niffler.service.UserDataClient;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserGraphqlController {

    private final UserDataClient userDataClient;

    @Autowired
    public UserGraphqlController(UserDataClient userDataClient) {
        this.userDataClient = userDataClient;
    }

    @SchemaMapping(typeName = "User", field = "friends")
    public List<UserJsonGQL> getFriends(UserJsonGQL user) {
        return getFriends(user.username());
    }

    @SchemaMapping(typeName = "User", field = "invitations")
    public List<UserJsonGQL> getInvitations(UserJsonGQL user) {
        return getInvitations(user.username());
    }

    @QueryMapping
    public UserJsonGQL user(@AuthenticationPrincipal Jwt principal,
                            @Nonnull DataFetchingEnvironment env) {
        checkSubQueries(env, 2, "friends", "invitations");
        String username = principal.getClaim("sub");
        UserJson userJson = userDataClient.currentUser(username);
        UserJsonGQL userJsonGQL = UserJsonGQL.fromUserJson(userJson);
        userJsonGQL.friends().addAll(getFriends(username));
        userJsonGQL.invitations().addAll(getInvitations(username));
        return userJsonGQL;
    }

    @QueryMapping
    public List<UserJsonGQL> users(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return userDataClient.allUsers(username).stream()
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
                input.currency(),
                input.photo(),
                null
        )));
    }

    @MutationMapping
    public UserJsonGQL addFriend(@AuthenticationPrincipal Jwt principal,
                                 @Argument String friendUsername) {
        String username = principal.getClaim("sub");
        FriendJson friend = new FriendJson(friendUsername);
        return UserJsonGQL.fromUserJson(userDataClient.addFriend(username, friend));
    }

    @MutationMapping
    public UserJsonGQL acceptInvitation(@AuthenticationPrincipal Jwt principal,
                                        @Argument String friendUsername) {
        String username = principal.getClaim("sub");
        FriendJson friend = new FriendJson(friendUsername);
        return UserJsonGQL.fromUserJson(userDataClient.acceptInvitationAndReturnFriend(username, friend));
    }

    @MutationMapping
    public UserJsonGQL declineInvitation(@AuthenticationPrincipal Jwt principal,
                                         @Argument String friendUsername) {
        String username = principal.getClaim("sub");
        FriendJson friend = new FriendJson(friendUsername);
        userDataClient.declineInvitation(username, friend);
        return UserJsonGQL.fromUserJson(userDataClient.allUsers(username)
                .stream()
                .filter(user -> user.username().equals(friendUsername))
                .findFirst()
                .orElseThrow());
    }

    @MutationMapping
    public UserJsonGQL removeFriend(@AuthenticationPrincipal Jwt principal,
                                    @Argument String friendUsername) {
        String username = principal.getClaim("sub");
        userDataClient.removeFriend(username, friendUsername);
        return UserJsonGQL.fromUserJson(userDataClient.allUsers(username)
                .stream()
                .filter(user -> user.username().equals(friendUsername))
                .findFirst()
                .orElseThrow());
    }

    private List<UserJsonGQL> getFriends(String username) {
        return userDataClient.friends(username, false)
                .stream()
                .map(UserJsonGQL::fromUserJson)
                .toList();
    }

    private List<UserJsonGQL> getInvitations(String username) {
        return userDataClient.invitations(username)
                .stream()
                .map(UserJsonGQL::fromUserJson)
                .collect(Collectors.toList());
    }

    private void checkSubQueries(@Nonnull DataFetchingEnvironment env, int depth, @Nonnull String... queryKeys) {
        for (String queryKey : queryKeys) {
            List<SelectedField> selectors = env.getSelectionSet().getFieldsGroupedByResultKey().get(queryKey);
            if (selectors != null && selectors.size() > depth) {
                throw new ToManySubQueriesException("Can`t fetch over 2 " + queryKey + " sub-queries");
            }
        }
    }
}
