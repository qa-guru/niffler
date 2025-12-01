package guru.qa.niffler.service;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class OauthSessionValidator {

  private static final String PRE_REQ_ATTR = "SPRING_SECURITY_SAVED_REQUEST";
  private static final String PRE_REQ_URI = "/oauth2/authorize";

  private final String nifflerFrontUri;
  private final String mobileCustomScheme;
  private final String androidAppUri;

  @Autowired
  public OauthSessionValidator(@Value("${niffler-front.base-uri}") String nifflerFrontUri,
                               @Value("${oauth2.mobile-custom-scheme}") String mobileCustomScheme,
                               @Value("${oauth2.android-app-uri}") String androidAppUri) {
    this.nifflerFrontUri = nifflerFrontUri;
    this.mobileCustomScheme = mobileCustomScheme;
    this.androidAppUri = androidAppUri;
  }

  public boolean isWebOauthSession(@Nonnull HttpSession session) {
    return isOauthSessionContainsRedirectUri(session, nifflerFrontUri);
  }

  public boolean isAndroidOauthSession(@Nonnull HttpSession session) {
    return isOauthSessionContainsRedirectUri(session, mobileCustomScheme + androidAppUri);
  }

  private boolean isOauthSessionContainsRedirectUri(@Nonnull HttpSession session, @Nonnull String redirectUri) {
    final DefaultSavedRequest savedRequest = (DefaultSavedRequest) session.getAttribute(PRE_REQ_ATTR);
    return savedRequest != null &&
        savedRequest.getRequestURI().equals(PRE_REQ_URI) &&
        Arrays.stream(savedRequest.getParameterValues("redirect_uri")).anyMatch(url -> url.contains(redirectUri));
  }
}
