package guru.qa.niffler.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.ObjectUtils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@ConditionalOnProperty(name = "firebase.enabled", havingValue = "true")
@Configuration
public class FirebaseConfig {

  @Bean
  public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
    return FirebaseMessaging.getInstance(firebaseApp);
  }

  @Bean
  public FirebaseApp firebaseApp(GoogleCredentials credentials) {
    return FirebaseApp.initializeApp(
        FirebaseOptions.builder()
            .setCredentials(credentials)
            .build()
    );
  }

  @Profile({"local"})
  @Bean
  public GoogleCredentials googleCredentialsLocal(@Value("${firebase.path}") String path) throws IOException {
    if (ObjectUtils.isEmpty(path)) {
      throw new IllegalStateException("GOOGLE_APPLICATION_CREDENTIALS ENV should be present for firebase.enabled=true");
    }
    try (InputStream serviceAccount = new FileInputStream(path)) {
      return GoogleCredentials.fromStream(serviceAccount);
    }
  }

  @Profile({"staging", "prod"})
  @Bean
  public GoogleCredentials googleCredentialsProd(@Value("${firebase.config}") String config) throws IOException {
    if (ObjectUtils.isEmpty(config)) {
      throw new IllegalStateException("firebase config should be present for firebase.enabled=true");
    }
    try (InputStream serviceAccount = new ByteArrayInputStream(config.getBytes(StandardCharsets.UTF_8))) {
      return GoogleCredentials.fromStream(serviceAccount);
    }
  }
}
