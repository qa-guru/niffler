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
@RequestMapping("/internal/v2/invitations")
public class InvitationsV2Controller {

    private static final Logger LOG = LoggerFactory.getLogger(InvitationsV2Controller.class);

    private final UserDataService userService;

    @Autowired
    public InvitationsV2Controller(UserDataService userService) {
        this.userService = userService;
    }

    @GetMapping("/income")
    public Page<? extends IUserJson> incomeInvitations(@RequestParam String username,
                                                       @PageableDefault Pageable pageable,
                                                       @RequestParam(required = false) String searchQuery) {
        return userService.incomeInvitations(username, pageable, searchQuery);
    }

    @GetMapping("/outcome")
    public Page<? extends IUserJson> outcomeInvitations(@RequestParam String username,
                                                        @PageableDefault Pageable pageable,
                                                        @RequestParam(required = false) String searchQuery) {
        return userService.outcomeInvitations(username, pageable, searchQuery);
    }
}
