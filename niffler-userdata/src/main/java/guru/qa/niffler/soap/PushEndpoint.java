package guru.qa.niffler.soap;

import guru.qa.niffler.service.PushTokenService;
import jaxb.userdata.RegisterPushTokenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


@Endpoint
public class PushEndpoint extends BaseEndpoint {

  private final PushTokenService tokenService;

  @Autowired
  public PushEndpoint(PushTokenService tokenService) {
    this.tokenService = tokenService;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "registerPushTokenRequest")
  @ResponsePayload
  public void updateUserRq(@RequestPayload RegisterPushTokenRequest request) {
    tokenService.upsert(request.getUsername(), request.getToken(), request.getUserAgent());
  }
}
