package niffler.controller;


import niffler.model.UserJson;
import niffler.service.api.RestUserDataClient;
import niffler.service.ws.SoapUserDataClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final RestUserDataClient restUserDataClient;
    private final SoapUserDataClient soapUserDataClient;

    @Autowired
    public UserController(RestUserDataClient restUserDataClient, SoapUserDataClient soapUserDataClient) {
        this.restUserDataClient = restUserDataClient;
        this.soapUserDataClient = soapUserDataClient;
    }

    @PostMapping("/updateUserInfo")
    public UserJson updateUserInfo(@AuthenticationPrincipal Jwt principal,
                                   @Validated @RequestBody UserJson user) {
        String username = principal.getClaim("sub");
        user.setUserName(username);
        return soapUserDataClient.updateUserInfo(user);
    }

    @GetMapping("/currentUser")
    public UserJson currentUser(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return soapUserDataClient.currentUser(username);
    }

    @GetMapping("/allUsers")
    public List<UserJson> allUsers(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return soapUserDataClient.allUsers(username);
    }
}
