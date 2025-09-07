package guru.qa.niffler.controller;

import guru.qa.niffler.model.FcmTokenJson;
import guru.qa.niffler.service.PushTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/push")
public class PushTokenController {

  private final PushTokenService tokenService;

  @Autowired
  public PushTokenController(PushTokenService tokenService) {
    this.tokenService = tokenService;
  }

  @PostMapping("/token")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void register(@RequestBody FcmTokenJson req) {
    tokenService.upsert(req.username(), req.token(), req.userAgent());
  }
}
