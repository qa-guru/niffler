package guru.qa.niffler.controller.graphql;


import guru.qa.niffler.model.SessionJson;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import java.util.Date;


@Controller
public class SessionQueryController {

  @QueryMapping
  public SessionJson session(@AuthenticationPrincipal Jwt principal) {
    if (principal != null) {
      return new SessionJson(
          principal.getClaim("sub"),
          Date.from(principal.getIssuedAt()),
          Date.from(principal.getExpiresAt())
      );
    } else {
      return SessionJson.empty();
    }
  }
}
