package guru.qa.niffler.controller.pageable;

import guru.qa.niffler.model.IUserJson;
import guru.qa.niffler.service.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/v2/users")
public class UserV2Controller {

    private static final Logger LOG = LoggerFactory.getLogger(UserV2Controller.class);

    private final UserDataService userService;

    @Autowired
    public UserV2Controller(UserDataService userService) {
        this.userService = userService;
    }


    @GetMapping("/all")
    public Page<? extends IUserJson> allUsers(@RequestParam String username,
                                              @PageableDefault Pageable pageable,
                                              @RequestParam(required = false) String searchQuery) {
        return userService.allUsers(username, pageable, searchQuery);
    }
}
