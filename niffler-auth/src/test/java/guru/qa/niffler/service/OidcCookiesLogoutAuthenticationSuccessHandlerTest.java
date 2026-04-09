package guru.qa.niffler.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OidcCookiesLogoutAuthenticationSuccessHandlerTest {

  @Test
  void onAuthenticationSuccessShouldInvokeDelegate(
      @Mock AuthenticationSuccessHandler delegate,
      @Mock HttpServletRequest request,
      @Mock HttpServletResponse response,
      @Mock Authentication authentication) throws Exception {
    OidcCookiesLogoutAuthenticationSuccessHandler handler =
        new OidcCookiesLogoutAuthenticationSuccessHandler(delegate, "XSRF-TOKEN");

    handler.onAuthenticationSuccess(request, response, authentication);

    verify(delegate, times(1)).onAuthenticationSuccess(eq(request), eq(response), eq(authentication));
  }

  @Test
  void onAuthenticationSuccessShouldClearNamedCookiesWithMaxAgeZero(
      @Mock AuthenticationSuccessHandler delegate,
      @Mock HttpServletRequest request,
      @Mock HttpServletResponse response,
      @Mock Authentication authentication) throws Exception {
    OidcCookiesLogoutAuthenticationSuccessHandler handler =
        new OidcCookiesLogoutAuthenticationSuccessHandler(delegate, "XSRF-TOKEN", "JSESSIONID");

    handler.onAuthenticationSuccess(request, response, authentication);

    ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
    verify(response, atLeastOnce()).addCookie(cookieCaptor.capture());

    List<Cookie> clearedCookies = cookieCaptor.getAllValues();
    List<String> clearedNames = clearedCookies.stream().map(Cookie::getName).toList();
    assertTrue(clearedNames.contains("XSRF-TOKEN"));
    assertTrue(clearedNames.contains("JSESSIONID"));
    clearedCookies.forEach(c -> assertEquals(0, c.getMaxAge()));
  }

  @Test
  void onAuthenticationSuccessShouldClearCookiesThenCallDelegate(
      @Mock AuthenticationSuccessHandler delegate,
      @Mock HttpServletRequest request,
      @Mock HttpServletResponse response,
      @Mock Authentication authentication) throws Exception {
    OidcCookiesLogoutAuthenticationSuccessHandler handler =
        new OidcCookiesLogoutAuthenticationSuccessHandler(delegate, "XSRF-TOKEN");

    handler.onAuthenticationSuccess(request, response, authentication);

    org.mockito.InOrder order = org.mockito.Mockito.inOrder(response, delegate);
    order.verify(response, atLeastOnce()).addCookie(any(Cookie.class));
    order.verify(delegate).onAuthenticationSuccess(request, response, authentication);
  }
}
