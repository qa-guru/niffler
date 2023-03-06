package niffler.controller;


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
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final RestUserDataClient restUserDataClient;

    public UserController(RestUserDataClient restUserDataClient) {
        this.restUserDataClient = restUserDataClient;
    }

    @PostMapping("/updateUserInfo")
    public UserJson updateUserInfo(@AuthenticationPrincipal Jwt principal,
                                   @Validated @RequestBody UserJson user) {
        String username = principal.getClaim("sub");
        user.setUserName(username);
        return restUserDataClient.updateUserInfo(user);
    }

    @GetMapping("/currentUser")
    public UserJson currentUser(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return restUserDataClient.currentUser(username);
    }

    @GetMapping("/allUsers")
    public List<UserJson> allUsers(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return restUserDataClient.allUsers(username);
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
                                           @RequestParam String inviteUsername) {
        String username = principal.getClaim("sub");
        return restUserDataClient.acceptInvitation(username, inviteUsername);
    }

    @PostMapping("/addFriend")
    public void addFriend(@AuthenticationPrincipal Jwt principal,
                          @RequestParam("username") String friendUsername) {
        String username = principal.getClaim("sub");
        restUserDataClient.addFriend(username, friendUsername);
    }

    @DeleteMapping("/removeFriend")
    public List<UserJson> removeFriend(@AuthenticationPrincipal Jwt principal,
                                       @RequestParam String friendUsername) {
        String username = principal.getClaim("sub");
        return restUserDataClient.removeFriend(username, friendUsername);
    }
}
