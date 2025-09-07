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
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import jakarta.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
@Service
@ConditionalOnProperty(name = "firebase.enabled", havingValue = "true")
public class FcmService implements IFcmService {
  private static final Logger LOG = LoggerFactory.getLogger(FcmService.class);

  private final FirebaseMessaging fcm;
  private final PushTokenService tokens;

  @Autowired
  public FcmService(FirebaseMessaging fcm, PushTokenService tokens) {
    this.fcm = fcm;
    this.tokens = tokens;
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
    final List<String> regTokens = tokens.tokensForUser(username);
    if (regTokens.isEmpty()) {
      LOG.info("No active tokens for user={}", username);
      return;
    }

    final WebpushConfig wp = webpushConfig(
        null,
        deeplink,
        iconUrl,
        3600,
        Urgecy.normal,
        true,
        title,
        body
    );

    MulticastMessage msg = MulticastMessage.builder()
        .putAllData(data == null ? Map.of() : data)
        .setWebpushConfig(wp)
        .addAllTokens(regTokens)
        .build();

    try {
      BatchResponse br = fcm.sendEachForMulticast(msg);
      handleBatchCleanup(regTokens, br);
      LOG.info("notifyUser={} successCount={}, failureCount={}",
          username, br.getSuccessCount(), br.getFailureCount());
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
    List<String> regTokens = tokens.tokensForUser(username);
    if (regTokens.isEmpty()) {
      LOG.info("No active tokens for user={}", username);
      return;
    }

    WebpushConfig wp = webpushConfig(
        topicHint,
        null,
        null,
        1800,
        Urgecy.high,
        false,
        null,
        null
    );

    MulticastMessage msg = MulticastMessage.builder()
        .putAllData(data == null ? Map.of() : data)
        .setWebpushConfig(wp)
        .addAllTokens(regTokens)
        .build();

    try {
      BatchResponse br = fcm.sendEachForMulticast(msg);
      handleBatchCleanup(regTokens, br);
    } catch (FirebaseMessagingException e) {
      LOG.error("FCM error data-only user={}", username, e);
    }
  }

  private WebpushConfig webpushConfig(@Nullable String topic,
                                      @Nullable String deeplink,
                                      @Nullable String iconUrl,
                                      @Nullable Integer ttlSeconds,
                                      @Nullable Urgecy urgency,
                                      boolean withNotification,
                                      @Nullable String title,
                                      @Nullable String body) {

    final WebpushConfig.Builder wb = WebpushConfig.builder();

    if (ttlSeconds != null) wb.putHeader("TTL", String.valueOf(ttlSeconds));
    if (urgency != null) wb.putHeader("Urgency", urgency.name);
    if (topic != null && !topic.isBlank()) wb.putHeader("Topic", topic);

    if (withNotification) {
      final WebpushNotification notification = WebpushNotification.builder()
          .setTitle(title)
          .setBody(body)
          .setIcon(iconUrl)
          .build();
      wb.setNotification(notification);
    }

    if (deeplink != null && !deeplink.isBlank()) {
      wb.setFcmOptions(WebpushFcmOptions.withLink(deeplink));
    }

    return wb.build();
  }

  private void handleBatchCleanup(List<String> tokensList, BatchResponse br) {
    for (int i = 0; i < br.getResponses().size(); i++) {
      SendResponse r = br.getResponses().get(i);
      if (!r.isSuccessful()) {
        FirebaseMessagingException ex = r.getException();
        String badToken = tokensList.get(i);
        if (isUnregistered(ex)) {
          tokens.deactivate(badToken);
          LOG.info("Deactivated dead token {}", badToken);
        } else {
          LOG.warn("FCM send error for token {}: {}", badToken,
              ex != null ? ex.getMessagingErrorCode() : "unknown");
        }
      }
    }
  }

  private boolean isUnregistered(@Nullable FirebaseMessagingException ex) {
    if (ex == null) {
      return false;
    }
    final MessagingErrorCode code = ex.getMessagingErrorCode();
    return code == MessagingErrorCode.UNREGISTERED
        || code == MessagingErrorCode.INVALID_ARGUMENT;
  }

  @RequiredArgsConstructor
  public enum Urgecy {
    very_low("very-low"),
    low("low"),
    normal("normal"),
    high("high");

    public final String name;
  }
}

