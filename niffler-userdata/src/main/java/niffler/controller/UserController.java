package niffler.controller;

import niffler.model.UserJson;
import niffler.service.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final UserDataService userService;

    @Autowired
    public UserController(UserDataService userService) {
        this.userService = userService;
    }

    @PostMapping("/updateUserInfo")
    public UserJson updateUserInfo(@RequestBody UserJson user) {
        return userService.update(user);
    }

    @GetMapping("/currentUser")
    public UserJson currentUser(@RequestParam String username) {
        return userService.getCurrentUserOrCreateIfAbsent(username);
    }

    @GetMapping("/allUsers")
    public List<UserJson> allUsers(@RequestParam String username) {
        return userService.allUsers(username);
    }

    @GetMapping("/friends")
    public List<UserJson> friends(@RequestParam String username,
                                  @RequestParam boolean includePending) {
        return userService.friends(username, includePending);
    }

    @GetMapping("/invitations")
    public List<UserJson> invitations(@RequestParam String username) {
        return userService.invitations(username);
    }

    @PostMapping("/acceptInvitation")
    public List<UserJson> acceptInvitation(@RequestParam String username,
                                           @RequestParam String inviteUsername) {
        return userService.acceptInvitation(username, inviteUsername);
    }

    @PostMapping("/addFriend")
    public void addFriend(@RequestParam String username,
                                    @RequestParam String friendUsername) {
        userService.addFriend(username, friendUsername);
    }

    @DeleteMapping("/removeFriend")
    public List<UserJson> removeFriend(@RequestParam String username,
                                       @RequestParam String friendUsername) {
        return userService.removeFriend(username, friendUsername);
    }
}
