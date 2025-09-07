package guru.qa.niffler.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Profile("!docker")
@Configuration
public class FirebaseConfig {

  @Bean
  public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
    return FirebaseMessaging.getInstance(firebaseApp);
  }

  @Bean
  public FirebaseApp firebaseApp(GoogleCredentials credentials) {
    FirebaseOptions options = FirebaseOptions.builder()
        .setCredentials(credentials)
        .build();

    return FirebaseApp.initializeApp(options);
  }

  @Profile({"local"})
  @Bean
  public GoogleCredentials googleCredentialsLocal(@Value("${firebase.path}") String path) throws IOException {
    try (InputStream serviceAccount = new FileInputStream(path)) {
      return GoogleCredentials.fromStream(serviceAccount);
    }
  }

  @Profile({"staging", "prod"})
  @Bean
  public GoogleCredentials googleCredentialsProd(@Value("${firebase.config}") String config) throws IOException {
    try (InputStream serviceAccount = new ByteArrayInputStream(config.getBytes(StandardCharsets.UTF_8))) {
      return GoogleCredentials.fromStream(serviceAccount);
    }
  }
}
