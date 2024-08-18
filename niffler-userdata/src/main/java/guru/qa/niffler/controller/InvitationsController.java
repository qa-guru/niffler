package guru.qa.niffler.controller;

import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/invitations")
public class InvitationsController {

  private static final Logger LOG = LoggerFactory.getLogger(InvitationsController.class);

  private final UserService userService;

  @Autowired
  public InvitationsController(UserService userService) {
    this.userService = userService;
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
