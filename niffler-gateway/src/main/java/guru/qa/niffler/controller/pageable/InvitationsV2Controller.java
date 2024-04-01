package guru.qa.niffler.controller.pageable;


import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserDataClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v2/invitations")
public class InvitationsV2Controller {

    private static final Logger LOG = LoggerFactory.getLogger(InvitationsV2Controller.class);

    private final UserDataClient userDataClient;

    @Autowired
    public InvitationsV2Controller(UserDataClient userDataClient) {
        this.userDataClient = userDataClient;
    }

    @GetMapping("/income")
    public Page<UserJson> incomeInvitations(@AuthenticationPrincipal Jwt principal,
                                            @PageableDefault Pageable pageable,
                                            @RequestParam(required = false) String searchQuery) {
        String username = principal.getClaim("sub");
        return userDataClient.incomeInvitations(username, pageable, searchQuery);
    }

    @GetMapping("/outcome")
    public Page<UserJson> outcomeInvitations(@AuthenticationPrincipal Jwt principal,
                                             @PageableDefault Pageable pageable,
                                             @RequestParam(required = false) String searchQuery) {
        String username = principal.getClaim("sub");
        return userDataClient.outcomeInvitations(username, pageable, searchQuery);
    }
}
