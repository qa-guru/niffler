package guru.qa.niffler.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@Service
@ConditionalOnProperty(name = "firebase.enabled", havingValue = "false", matchIfMissing = true)
@ParametersAreNonnullByDefault
public class NoopFcmService implements IFcmService {
  private static final Logger LOG = LoggerFactory.getLogger(NoopFcmService.class);

  @Override
  public void notifyUser(String username, @Nullable String title, @Nullable String body, @Nullable Map<String, String> data, @Nullable String deeplink, @Nullable String iconUrl) {
    LOG.debug("[NOOP] notifyUser username={}, title={}", username, title);
  }

  @Override
  public void notifyUserDataOnly(String username, @Nullable Map<String, String> data, @Nullable String topicHint) {
    LOG.debug("[NOOP] notifyUserDataOnly username={}, topic={}", username, topicHint);
  }
}
