package niffler.controller;


import niffler.model.FriendJson;
import niffler.model.UserJson;
import niffler.service.api.RestUserDataClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class FriendsController {

    private static final Logger LOG = LoggerFactory.getLogger(FriendsController.class);

    private final RestUserDataClient restUserDataClient;

    public FriendsController(RestUserDataClient restUserDataClient) {
        this.restUserDataClient = restUserDataClient;
    }

    @GetMapping("/friends")
    public List<UserJson> friends(@AuthenticationPrincipal Jwt principal,
                                  @RequestParam boolean includePending) {
        String username = principal.getClaim("sub");
        return restUserDataClient.friends(username, includePending);
    }

    @GetMapping("/invitations")
    public List<UserJson> invitations(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return restUserDataClient.invitations(username);
    }

    @PostMapping("/acceptInvitation")
    public List<UserJson> acceptInvitation(@AuthenticationPrincipal Jwt principal,
                                           @Validated @RequestBody FriendJson invitation) {
        String username = principal.getClaim("sub");
        return restUserDataClient.acceptInvitation(username, invitation);
    }

    @PostMapping("/declineInvitation")
    public List<UserJson> declineInvitation(@AuthenticationPrincipal Jwt principal,
                                            @Validated @RequestBody FriendJson invitation) {
        String username = principal.getClaim("sub");
        return restUserDataClient.declineInvitation(username, invitation);
    }

    @PostMapping("/addFriend")
    public void addFriend(@AuthenticationPrincipal Jwt principal,
                          @Validated @RequestBody FriendJson friend) {
        String username = principal.getClaim("sub");
        restUserDataClient.addFriend(username, friend);
    }

    @DeleteMapping("/removeFriend")
    public List<UserJson> removeFriend(@AuthenticationPrincipal Jwt principal,
                                       @RequestParam("username") String friendUsername) {
        String username = principal.getClaim("sub");
        return restUserDataClient.removeFriend(username, friendUsername);
    }
}
