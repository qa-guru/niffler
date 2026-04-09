package guru.qa.niffler.service;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OauthSessionValidatorTest {

  private static final String FRONT_URI = "http://localhost:3000";
  private static final String MOBILE_SCHEME = "app://";
  private static final String ANDROID_APP_URI = "ru.niffler_android";

  private OauthSessionValidator validator;

  @BeforeEach
  void setUp() {
    validator = new OauthSessionValidator(FRONT_URI, MOBILE_SCHEME, ANDROID_APP_URI);
  }

  @Test
  void isWebOauthSessionReturnsTrueWhenSavedRequestHasFrontUriRedirect(
      @Mock HttpSession session,
      @Mock DefaultSavedRequest savedRequest) {
    when(session.getAttribute(eq("SPRING_SECURITY_SAVED_REQUEST"))).thenReturn(savedRequest);
    when(savedRequest.getRequestURI()).thenReturn("/oauth2/authorize");
    when(savedRequest.getParameterValues("redirect_uri"))
        .thenReturn(new String[]{FRONT_URI + "/authorized"});

    assertTrue(validator.isWebOauthSession(session));
  }

  @Test
  void isWebOauthSessionReturnsFalseWhenNoSavedRequest(@Mock HttpSession session) {
    when(session.getAttribute(eq("SPRING_SECURITY_SAVED_REQUEST"))).thenReturn(null);

    assertFalse(validator.isWebOauthSession(session));
  }

  @Test
  void isWebOauthSessionReturnsFalseWhenSavedRequestUriIsNotOauth2Authorize(
      @Mock HttpSession session,
      @Mock DefaultSavedRequest savedRequest) {
    when(session.getAttribute(eq("SPRING_SECURITY_SAVED_REQUEST"))).thenReturn(savedRequest);
    when(savedRequest.getRequestURI()).thenReturn("/some/other/path");

    assertFalse(validator.isWebOauthSession(session));
  }

  @Test
  void isWebOauthSessionReturnsFalseWhenRedirectUriDoesNotContainFrontUri(
      @Mock HttpSession session,
      @Mock DefaultSavedRequest savedRequest) {
    when(session.getAttribute(eq("SPRING_SECURITY_SAVED_REQUEST"))).thenReturn(savedRequest);
    when(savedRequest.getRequestURI()).thenReturn("/oauth2/authorize");
    when(savedRequest.getParameterValues("redirect_uri"))
        .thenReturn(new String[]{"http://other-domain.com/callback"});

    assertFalse(validator.isWebOauthSession(session));
  }

  @Test
  void isAndroidOauthSessionReturnsTrueWhenSavedRequestHasMobileSchemeRedirect(
      @Mock HttpSession session,
      @Mock DefaultSavedRequest savedRequest) {
    when(session.getAttribute(eq("SPRING_SECURITY_SAVED_REQUEST"))).thenReturn(savedRequest);
    when(savedRequest.getRequestURI()).thenReturn("/oauth2/authorize");
    when(savedRequest.getParameterValues("redirect_uri"))
        .thenReturn(new String[]{MOBILE_SCHEME + ANDROID_APP_URI + "/callback"});

    assertTrue(validator.isAndroidOauthSession(session));
  }

  @Test
  void isAndroidOauthSessionReturnsFalseWhenNoSavedRequest(@Mock HttpSession session) {
    when(session.getAttribute(eq("SPRING_SECURITY_SAVED_REQUEST"))).thenReturn(null);

    assertFalse(validator.isAndroidOauthSession(session));
  }

  @Test
  void isAndroidOauthSessionReturnsFalseWhenRedirectUriDoesNotMatchMobileScheme(
      @Mock HttpSession session,
      @Mock DefaultSavedRequest savedRequest) {
    when(session.getAttribute(eq("SPRING_SECURITY_SAVED_REQUEST"))).thenReturn(savedRequest);
    when(savedRequest.getRequestURI()).thenReturn("/oauth2/authorize");
    when(savedRequest.getParameterValues("redirect_uri"))
        .thenReturn(new String[]{FRONT_URI + "/authorized"});

    assertFalse(validator.isAndroidOauthSession(session));
  }

  @Test
  void isAndroidOauthSessionReturnsFalseWhenRequestUriIsNotOauth2Authorize(
      @Mock HttpSession session,
      @Mock DefaultSavedRequest savedRequest) {
    when(session.getAttribute(eq("SPRING_SECURITY_SAVED_REQUEST"))).thenReturn(savedRequest);
    when(savedRequest.getRequestURI()).thenReturn("/login");

    assertFalse(validator.isAndroidOauthSession(session));
  }
}
