package guru.qa.niffler.api.client;

import guru.qa.niffler.api.context.CookieContext;
import guru.qa.niffler.api.context.SessionContext;
import guru.qa.niffler.api.interceptor.AddCookiesInterceptor;
import guru.qa.niffler.api.interceptor.RecievedCodeInterceptor;
import guru.qa.niffler.api.interceptor.RecievedCookiesInterceptor;
import guru.qa.niffler.api.service.AuthService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AuthClient extends BaseRestClient7 {

  public AuthClient() {
    super(
        CFG.getAuthUrl(),
        true,
        new RecievedCookiesInterceptor(),
        new AddCookiesInterceptor(),
        new RecievedCodeInterceptor()
    );
  }

  private final AuthService authService = retrofit.create(AuthService.class);

  public void authorizePreRequest() {
    try {
      authService.authorize(
          "code",
          "client",
          "openid",
          CFG.getFrontUrl() + "/authorized",
          SessionContext.getInstance().getCodeChallenge(),
          "S256"
      ).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  public void login(String username, String password) {
    try {
      authService.login(
          CookieContext.getInstance().getFormattedCookie("JSESSIONID"),
          CookieContext.getInstance().getFormattedCookie("XSRF-TOKEN"),
          CookieContext.getInstance().getCookie("XSRF-TOKEN"),
          username,
          password
      ).execute();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  public String getToken() {
    SessionContext sessionContext = SessionContext.getInstance();
    try {
      return authService.token(
          "Basic " + Base64.getEncoder().encodeToString("client:secret".getBytes(StandardCharsets.UTF_8)),
          "client",
          CFG.getFrontUrl() + "/authorized",
          "authorization_code",
          sessionContext.getCode(),
          sessionContext.getCodeVerifier()
      ).execute().body().get("id_token").asText();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
