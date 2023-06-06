package guru.qa.niffler.api;

import guru.qa.niffler.api.context.CookieContext;
import guru.qa.niffler.api.context.SessionContext;
import guru.qa.niffler.api.interceptor.AddCookiesInterceptor;
import guru.qa.niffler.api.interceptor.RecievedCodeInterceptor;
import guru.qa.niffler.api.interceptor.RecievedCookiesInterceptor;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RetrofitAuthClient extends BaseRestClient implements AuthClient {

  public RetrofitAuthClient() {
    super(
        CFG.getAuthUrl(),
        true,
        new RecievedCookiesInterceptor(),
        new AddCookiesInterceptor(),
        new RecievedCodeInterceptor()
    );
  }

  private final AuthService authService = retrofit.create(AuthService.class);

  @Override
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

  @Override
  public void login(String username, String password) {
    final CookieContext cookieContext = CookieContext.getInstance();

    try {
      authService.login(
          cookieContext.getFormattedCookie("JSESSIONID"),
          cookieContext.getFormattedCookie("XSRF-TOKEN"),
          cookieContext.getCookie("XSRF-TOKEN"),
          username,
          password
      ).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getToken() {
    final SessionContext sessionContext = SessionContext.getInstance();

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
