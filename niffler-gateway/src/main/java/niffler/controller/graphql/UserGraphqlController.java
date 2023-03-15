package niffler.controller.graphql;

import jakarta.validation.Valid;
import niffler.model.FriendJson;
import niffler.model.UserJson;
import niffler.model.graphql.UpdateUserInfoInput;
import niffler.model.graphql.UserJsonGQL;
import niffler.service.api.RestUserDataClient;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserGraphqlController {

    private final RestUserDataClient restUserDataClient;

    public UserGraphqlController(RestUserDataClient restUserDataClient) {
        this.restUserDataClient = restUserDataClient;
    }

    @QueryMapping
    public UserJsonGQL user(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        UserJson userJson = restUserDataClient.currentUser(username);
        UserJsonGQL userJsonGQL = UserJsonGQL.fromUserJson(userJson);
        userJsonGQL.setFriends(getFriends(username));
        userJsonGQL.setInvitations(getInvitations(username));
        return userJsonGQL;
    }
     private List<UserJsonGQL> getFriends(String username) {
        return restUserDataClient.friends(username, false)
                .stream()
                .map(friend -> UserJsonGQL.fromUserJson(friend))
                .collect(Collectors.toList());
     }

    private List<UserJsonGQL> getInvitations(String username) {
        return restUserDataClient.invitations(username)
                .stream()
                .map(friend -> UserJsonGQL.fromUserJson(friend))
                .collect(Collectors.toList());
    }

    @QueryMapping
    public List<UserJsonGQL> users(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return restUserDataClient.allUsers(username).stream()
                .map(friend -> UserJsonGQL.fromUserJson(friend))
                .collect(Collectors.toList());
    }

    @MutationMapping
    public UserJsonGQL updateUser(@AuthenticationPrincipal Jwt principal,
                                   @Argument @Valid UpdateUserInfoInput input) {
        String username = principal.getClaim("sub");
        UserJson user = UserJson.fromUpdateUserInfoInput(input);
        user.setUsername(username);
        return UserJsonGQL.fromUserJson(restUserDataClient.updateUserInfo(user));
    }

    @MutationMapping
    public UserJsonGQL addFriend(@AuthenticationPrincipal Jwt principal,
                                 @Argument String friendUsername) {
        String username = principal.getClaim("sub");
        FriendJson friend = new FriendJson();
        friend.setUsername(friendUsername);
        return UserJsonGQL.fromUserJson(restUserDataClient.addFriend(username, friend));
    }

    @MutationMapping
    public UserJsonGQL acceptInvitation(@AuthenticationPrincipal Jwt principal,
                                           @Argument String friendUsername) {
        String username = principal.getClaim("sub");
        FriendJson friend = new FriendJson();
        friend.setUsername(friendUsername);
        restUserDataClient.acceptInvitation(username, friend);
        return null;
    }
}
