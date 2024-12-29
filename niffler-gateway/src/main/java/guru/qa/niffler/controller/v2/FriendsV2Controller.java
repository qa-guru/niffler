package guru.qa.niffler.controller.v2;


import guru.qa.niffler.config.NifflerGatewayServiceConfig;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserDataClient;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/api/v2/friends")
@SecurityRequirement(name = NifflerGatewayServiceConfig.OPEN_API_AUTH_SCHEME)
public class FriendsV2Controller {

  private static final Logger LOG = LoggerFactory.getLogger(FriendsV2Controller.class);

  private final UserDataClient userDataClient;

  @Autowired
  public FriendsV2Controller(UserDataClient userDataClient) {
    this.userDataClient = userDataClient;
  }

  @GetMapping("/all")
  public Page<UserJson> friends(@AuthenticationPrincipal Jwt principal,
                                @PageableDefault Pageable pageable,
                                @RequestParam(required = false) String searchQuery) {
    String username = principal.getClaim("sub");
    return userDataClient.friends(username, pageable, searchQuery);
  }
}
