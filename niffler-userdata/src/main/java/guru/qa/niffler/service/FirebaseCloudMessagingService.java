package guru.qa.niffler.service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.SendResponse;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushFcmOptions;
import com.google.firebase.messaging.WebpushNotification;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
@Service
@ConditionalOnProperty(name = "firebase.enabled", havingValue = "true")
public class FirebaseCloudMessagingService implements MessagingService {
  private static final Logger LOG = LoggerFactory.getLogger(FirebaseCloudMessagingService.class);

  private final FirebaseMessaging fcm;
  private final PushTokenService pushTokenService;

  @Autowired
  public FirebaseCloudMessagingService(FirebaseMessaging fcm, PushTokenService pushTokenService) {
    this.fcm = fcm;
    this.pushTokenService = pushTokenService;
  }

  /**
   * Send Web push for user
   */
  @Override
  public void notifyUser(String username,
                         @Nullable String title,
                         @Nullable String body,
                         @Nullable Map<String, String> data,
                         @Nullable String deeplink,
                         @Nullable String iconUrl) {
    final List<String> regTokens = pushTokenService.tokensForUser(username);
    if (regTokens.isEmpty()) {
      LOG.info("No active tokens for user={}", username);
      return;
    }

    final WebpushConfig webpushConfig = webpushConfig(
        null,
        deeplink,
        iconUrl,
        3600,
        Urgency.normal,
        true,
        title,
        body
    );

    final MulticastMessage msg = MulticastMessage.builder()
        .putAllData(data == null ? Map.of() : data)
        .setWebpushConfig(webpushConfig)
        .addAllTokens(regTokens)
        .build();

    try {
      final BatchResponse batchResponse = fcm.sendEachForMulticast(msg);
      handleBatchCleanup(regTokens, batchResponse);
      LOG.info("notifyUser={} successCount={}, failureCount={}",
          username, batchResponse.getSuccessCount(), batchResponse.getFailureCount());
    } catch (FirebaseMessagingException e) {
      LOG.error("FCM error notifyUser={}", username, e);
    }
  }

  /**
   * Data-only push
   */
  @Override
  public void notifyUserDataOnly(String username,
                                 @Nullable Map<String, String> data,
                                 @Nullable String topicHint) {
    final List<String> regTokens = pushTokenService.tokensForUser(username);
    if (regTokens.isEmpty()) {
      LOG.info("No active tokens for user={}", username);
      return;
    }

    final WebpushConfig webpushConfig = webpushConfig(
        topicHint,
        null,
        null,
        1800,
        Urgency.high,
        false,
        null,
        null
    );

    final MulticastMessage msg = MulticastMessage.builder()
        .putAllData(data == null ? Map.of() : data)
        .setWebpushConfig(webpushConfig)
        .addAllTokens(regTokens)
        .build();

    try {
      final BatchResponse batchResponse = fcm.sendEachForMulticast(msg);
      handleBatchCleanup(regTokens, batchResponse);
    } catch (FirebaseMessagingException e) {
      LOG.error("FCM error data-only user={}", username, e);
    }
  }

  private WebpushConfig webpushConfig(@Nullable String topic,
                                      @Nullable String deeplink,
                                      @Nullable String iconUrl,
                                      @Nullable Integer ttlSeconds,
                                      @Nullable Urgency urgency,
                                      boolean withNotification,
                                      @Nullable String title,
                                      @Nullable String body) {

    final WebpushConfig.Builder webpushBuilder = WebpushConfig.builder();

    if (ttlSeconds != null) {
      webpushBuilder.putHeader("TTL", String.valueOf(ttlSeconds));
    }
    if (urgency != null) {
      webpushBuilder.putHeader("Urgency", urgency.name);
    }
    if (topic != null && !topic.isBlank()) {
      webpushBuilder.putHeader("Topic", topic);
    }
    if (withNotification) {
      final WebpushNotification notification = WebpushNotification.builder()
          .setTitle(title)
          .setBody(body)
          .setIcon(iconUrl)
          .build();
      webpushBuilder.setNotification(notification);
    }
    if (deeplink != null && !deeplink.isBlank()) {
      webpushBuilder.setFcmOptions(WebpushFcmOptions.withLink(deeplink));
    }

    return webpushBuilder.build();
  }

  private void handleBatchCleanup(List<String> tokensList, BatchResponse batchResponse) {
    for (int i = 0; i < batchResponse.getResponses().size(); i++) {
      final SendResponse sendResponse = batchResponse.getResponses().get(i);
      if (!sendResponse.isSuccessful()) {
        final FirebaseMessagingException ex = sendResponse.getException();
        final String badToken = tokensList.get(i);
        if (isUnregistered(ex)) {
          pushTokenService.deactivate(badToken);
          LOG.info("Deactivated dead token {}", badToken);
        } else {
          LOG.warn("FCM send error for token {}: {}", badToken,
              ex != null
                  ? ex.getMessagingErrorCode()
                  : "unknown"
          );
        }
      }
    }
  }

  private boolean isUnregistered(@Nullable FirebaseMessagingException ex) {
    if (ex == null) {
      return false;
    }
    final MessagingErrorCode code = ex.getMessagingErrorCode();
    return code == MessagingErrorCode.UNREGISTERED || code == MessagingErrorCode.INVALID_ARGUMENT;
  }

  @RequiredArgsConstructor
  public enum Urgency {
    very_low("very-low"),
    low("low"),
    normal("normal"),
    high("high");

    public final String name;
  }
}
