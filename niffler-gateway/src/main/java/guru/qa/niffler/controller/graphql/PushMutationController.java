package guru.qa.niffler.controller.graphql;

import guru.qa.niffler.model.FcmTokenJson;
import guru.qa.niffler.model.gql.FcmTokenGqlInput;
import guru.qa.niffler.service.UserDataClient;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

@Controller
@PreAuthorize("isAuthenticated()")
public class PushMutationController {

  private final UserDataClient userDataClient;

  @Autowired
  public PushMutationController(UserDataClient userDataClient) {
    this.userDataClient = userDataClient;
  }

  @MutationMapping
  public Boolean fcmToken(@AuthenticationPrincipal Jwt principal,
                          @Valid @Argument FcmTokenGqlInput input) {
    final String principalUsername = principal.getClaim("sub");
    userDataClient.registerToken(new FcmTokenJson(
        principalUsername,
        input.token(),
        input.userAgent()
    ));
    return null;
  }
}
