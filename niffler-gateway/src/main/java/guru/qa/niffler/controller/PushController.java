package guru.qa.niffler.controller;

import guru.qa.niffler.model.FcmTokenJson;
import guru.qa.niffler.service.UserDataClient;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/push")
public class PushController {

  private final UserDataClient userDataClient;

  @Autowired
  public PushController(UserDataClient userDataClient) {
    this.userDataClient = userDataClient;
  }

  @PostMapping("/token")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void registerToken(@AuthenticationPrincipal Jwt principal,
                            @Valid @RequestBody FcmTokenJson fcmTokenJson) {
    final String principalUsername = principal.getClaim("sub");
    userDataClient.registerToken(fcmTokenJson.addUsername(principalUsername));
  }
}
