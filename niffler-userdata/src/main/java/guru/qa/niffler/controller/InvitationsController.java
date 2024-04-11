package guru.qa.niffler.controller;

import guru.qa.niffler.model.IUserJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal/invitations")
public class InvitationsController {

    private static final Logger LOG = LoggerFactory.getLogger(InvitationsController.class);

    private final UserDataService userService;

    @Autowired
    public InvitationsController(UserDataService userService) {
        this.userService = userService;
    }

    @GetMapping("/income")
    public List<? extends IUserJson> incomeInvitations(@RequestParam String username,
                                                       @RequestParam(required = false) String searchQuery) {
        return userService.incomeInvitations(username, searchQuery);
    }

    @GetMapping("/outcome")
    public List<? extends IUserJson> outcomeInvitations(@RequestParam String username,
                                                        @RequestParam(required = false) String searchQuery) {
        return userService.outcomeInvitations(username, searchQuery);
    }

    @PostMapping("/send")
    public UserJson sendInvitation(@RequestParam String username,
                                   @RequestParam String targetUsername) {
        return userService.createFriendshipRequest(username, targetUsername);
    }

    @PostMapping("/accept")
    public UserJson acceptInvitation(@RequestParam String username,
                                     @RequestParam String targetUsername) {
        return userService.acceptFriendshipRequest(username, targetUsername);
    }

    @PostMapping("/decline")
    public UserJson declineInvitation(@RequestParam String username,
                                      @RequestParam String targetUsername) {
        return userService.declineFriendshipRequest(username, targetUsername);
    }
}
