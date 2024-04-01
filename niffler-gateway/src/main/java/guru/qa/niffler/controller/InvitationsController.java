package guru.qa.niffler.controller;


import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserDataClient;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/invitations")
public class InvitationsController {

    private static final Logger LOG = LoggerFactory.getLogger(InvitationsController.class);

    private final UserDataClient userDataClient;

    @Autowired
    public InvitationsController(UserDataClient userDataClient) {
        this.userDataClient = userDataClient;
    }

    @GetMapping("/income")
    public List<UserJson> incomeInvitations(@AuthenticationPrincipal Jwt principal,
                                            @RequestParam(required = false) String searchQuery) {
        String username = principal.getClaim("sub");
        return userDataClient.incomeInvitations(username, searchQuery);
    }

    @GetMapping("/outcome")
    public List<UserJson> outcomeInvitations(@AuthenticationPrincipal Jwt principal,
                                             @RequestParam(required = false) String searchQuery) {
        String username = principal.getClaim("sub");
        return userDataClient.outcomeInvitations(username, searchQuery);
    }

    @PostMapping("/send")
    public UserJson sendInvitation(@AuthenticationPrincipal Jwt principal,
                                   @Valid @RequestBody FriendJson friend) {
        String username = principal.getClaim("sub");
        return userDataClient.sendInvitation(username, friend.username());
    }

    @PostMapping("/accept")
    public UserJson acceptInvitation(@AuthenticationPrincipal Jwt principal,
                                     @Valid @RequestBody FriendJson invitation) {
        String username = principal.getClaim("sub");
        return userDataClient.acceptInvitation(username, invitation.username());
    }

    @PostMapping("/decline")
    public UserJson declineInvitation(@AuthenticationPrincipal Jwt principal,
                                      @Valid @RequestBody FriendJson invitation) {
        String username = principal.getClaim("sub");
        return userDataClient.declineInvitation(username, invitation.username());
    }
}
