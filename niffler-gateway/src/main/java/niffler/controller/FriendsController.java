package niffler.controller;


import niffler.model.FriendJson;
import niffler.model.UserJson;
import niffler.service.UserDataClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private final UserDataClient userDataClient;

    @Autowired
    public FriendsController(@Qualifier("rest") UserDataClient userDataClient) {
        this.userDataClient = userDataClient;
    }

    @GetMapping("/friends")
    public List<UserJson> friends(@AuthenticationPrincipal Jwt principal,
                                  @RequestParam boolean includePending) {
        String username = principal.getClaim("sub");
        return userDataClient.friends(username, includePending);
    }

    @GetMapping("/invitations")
    public List<UserJson> invitations(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return userDataClient.invitations(username);
    }

    @PostMapping("/acceptInvitation")
    public List<UserJson> acceptInvitation(@AuthenticationPrincipal Jwt principal,
                                           @Validated @RequestBody FriendJson invitation) {
        String username = principal.getClaim("sub");
        return userDataClient.acceptInvitation(username, invitation);
    }

    @PostMapping("/declineInvitation")
    public List<UserJson> declineInvitation(@AuthenticationPrincipal Jwt principal,
                                            @Validated @RequestBody FriendJson invitation) {
        String username = principal.getClaim("sub");
        return userDataClient.declineInvitation(username, invitation);
    }

    @PostMapping("/addFriend")
    public UserJson addFriend(@AuthenticationPrincipal Jwt principal,
                              @Validated @RequestBody FriendJson friend) {
        String username = principal.getClaim("sub");
        return userDataClient.addFriend(username, friend);
    }

    @DeleteMapping("/removeFriend")
    public List<UserJson> removeFriend(@AuthenticationPrincipal Jwt principal,
                                       @RequestParam("username") String friendUsername) {
        String username = principal.getClaim("sub");
        return userDataClient.removeFriend(username, friendUsername);
    }
}
