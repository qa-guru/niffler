package guru.qa.niffler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private static final String LOGIN_VIEW_NAME = "login";

    @GetMapping("/login")
    public String login() {
        return LOGIN_VIEW_NAME;
    }
}
