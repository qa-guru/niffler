package guru.qa.niffler.controller;

import guru.qa.niffler.service.OauthSessionValidator;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class LoginController {

  private static final String LOGIN_VIEW_NAME = "login";
  private static final String MODEL_FRONT_URI_ATTR = "frontUri";
  private static final String MODEL_AUTH_URI_ATTR = "authUri";

  private final String nifflerFrontUri;
  private final String nifflerAuthUri;
  private final OauthSessionValidator sessionValidator;

  public LoginController(@Value("${niffler-front.base-uri}") String nifflerFrontUri,
                         @Value("${niffler-auth.base-uri}") String nifflerAuthUri,
                         OauthSessionValidator sessionValidator) {
    this.nifflerFrontUri = nifflerFrontUri;
    this.nifflerAuthUri = nifflerAuthUri;
    this.sessionValidator = sessionValidator;
  }

  @GetMapping("/login")
  public String login(HttpSession session, Model model) {
    if (sessionValidator.isWebOauthSession(session) || sessionValidator.isAndroidOauthSession(session)) {
      model.addAttribute(MODEL_AUTH_URI_ATTR, nifflerAuthUri);
      model.addAttribute(MODEL_FRONT_URI_ATTR, nifflerFrontUri);
      return LOGIN_VIEW_NAME;
    }
    return "redirect:" + nifflerFrontUri;
  }

  @GetMapping("/")
  @ResponseStatus(HttpStatus.FOUND)
  public String root(Authentication authentication) {
    if (authentication != null && authentication.isAuthenticated()) {
      return "redirect:" + nifflerFrontUri;
    }
    return "redirect:/" + LOGIN_VIEW_NAME;
  }
}
