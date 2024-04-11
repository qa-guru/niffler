package guru.qa.niffler.controller;

import guru.qa.niffler.model.IUserJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal/users")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserDataService userService;

    @Autowired
    public UserController(UserDataService userService) {
        this.userService = userService;
    }

    @GetMapping("/current")
    public UserJson currentUser(@RequestParam String username) {
        return userService.getCurrentUser(username);
    }

    @GetMapping("/all")
    public List<? extends IUserJson> allUsers(@RequestParam String username,
                                              @RequestParam(required = false) String searchQuery) {
        return userService.allUsers(username, searchQuery);
    }

    @PostMapping("/update")
    public UserJson updateUserInfo(@RequestBody UserJson user) {
        return userService.update(user);
    }
}
