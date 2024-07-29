package guru.qa.niffler.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private static final String LOGIN_VIEW_NAME = "login";
    private static final String PRE_REQUEST_ATTR = "SPRING_SECURITY_SAVED_REQUEST";
    private static final String PRE_REQUEST_URI = "/oauth2/authorize";

    private final String frontUrl;

    public LoginController(@Value("${niffler-front.base-uri}")String frontUrl) {
        this.frontUrl = frontUrl;
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        DefaultSavedRequest preRequest = (DefaultSavedRequest) session.getAttribute(PRE_REQUEST_ATTR);
        if (preRequest == null || !preRequest.getRequestURI().equals(PRE_REQUEST_URI)) {
            return "redirect:" + frontUrl;
        }
        return LOGIN_VIEW_NAME;
    }
}
