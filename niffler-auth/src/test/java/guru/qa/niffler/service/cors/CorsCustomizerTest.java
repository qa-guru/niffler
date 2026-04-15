package guru.qa.niffler.service.cors;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CorsCustomizerTest {

  private static final String FRONT_URI = "http://localhost:3000";
  private static final String AUTH_URI = "http://localhost:9000";

  private final CorsCustomizer corsCustomizer = new CorsCustomizer(FRONT_URI, AUTH_URI);

  @Test
  void allowedOriginsReturnsExactlyConfiguredUris() {
    Set<String> origins = corsCustomizer.allowedOrigins();
    assertEquals(2, origins.size());
    assertEquals(Set.of(FRONT_URI, AUTH_URI), origins);
  }
}
