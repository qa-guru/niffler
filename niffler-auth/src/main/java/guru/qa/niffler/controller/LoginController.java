package guru.qa.niffler.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private static final String LOGIN_VIEW_NAME = "login";
    private final String nifflerFrontUri;

    public LoginController(@Value("${niffler-front.base-uri}") String nifflerFrontUri) {
        this.nifflerFrontUri = nifflerFrontUri;
    }

    @GetMapping("/login")
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (((WebAuthenticationDetails) authentication.getDetails()).getSessionId() == null) {
            return "redirect:" + nifflerFrontUri;
        }
        return LOGIN_VIEW_NAME;
    }
}
