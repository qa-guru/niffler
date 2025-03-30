package guru.qa.niffler.controller.v3;


import guru.qa.niffler.config.NifflerGatewayServiceConfig;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserDataClient;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v3/friends")
@SecurityRequirement(name = NifflerGatewayServiceConfig.OPEN_API_AUTH_SCHEME)
public class FriendsV3Controller {

  private static final Logger LOG = LoggerFactory.getLogger(FriendsV3Controller.class);

  private final UserDataClient userDataClient;

  @Autowired
  public FriendsV3Controller(UserDataClient userDataClient) {
    this.userDataClient = userDataClient;
  }

  @GetMapping("/all")
  public PagedModel<UserJson> friends(@AuthenticationPrincipal Jwt principal,
                                      @PageableDefault Pageable pageable,
                                      @RequestParam(required = false) String searchQuery) {
    final String principalUsername = principal.getClaim("sub");
    return userDataClient.friendsV3(principalUsername, pageable, searchQuery);
  }
}
