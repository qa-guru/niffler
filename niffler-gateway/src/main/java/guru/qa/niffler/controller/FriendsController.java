package guru.qa.niffler.controller;


import guru.qa.niffler.config.NifflerGatewayServiceConfig;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserDataClient;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/friends")
@SecurityRequirement(name = NifflerGatewayServiceConfig.OPEN_API_AUTH_SCHEME)
public class FriendsController {

  private static final Logger LOG = LoggerFactory.getLogger(FriendsController.class);

  private final UserDataClient userDataClient;

  @Autowired
  public FriendsController(UserDataClient userDataClient) {
    this.userDataClient = userDataClient;
  }

  @GetMapping("/all")
  public List<UserJson> friends(@AuthenticationPrincipal Jwt principal,
                                @RequestParam(required = false) String searchQuery) {
    String username = principal.getClaim("sub");
    return userDataClient.friends(username, searchQuery);
  }

  @DeleteMapping("/remove")
  public void removeFriend(@AuthenticationPrincipal Jwt principal,
                           @RequestParam("username") String targetUsername) {
    String username = principal.getClaim("sub");
    userDataClient.removeFriend(username, targetUsername);
  }
}
