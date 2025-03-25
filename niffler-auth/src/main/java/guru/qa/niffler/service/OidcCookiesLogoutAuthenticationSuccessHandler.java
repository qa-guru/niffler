package guru.qa.niffler.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.io.IOException;

public class OidcCookiesLogoutAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final AuthenticationSuccessHandler delegate;
  private final LogoutHandler cookieHandler;

  public OidcCookiesLogoutAuthenticationSuccessHandler(AuthenticationSuccessHandler delegate, String... cookieNames) {
    this.delegate = delegate;
    this.cookieHandler = new CookieClearingLogoutHandler(cookieNames);

  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    this.cookieHandler.logout(request, response, authentication);
    this.delegate.onAuthenticationSuccess(request, response, authentication);
  }
}
