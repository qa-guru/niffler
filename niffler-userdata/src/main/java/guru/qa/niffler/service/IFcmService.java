package guru.qa.niffler.service;

import jakarta.annotation.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
public interface IFcmService {

  void notifyUser(String username,
                         @Nullable String title,
                         @Nullable String body,
                         @Nullable Map<String, String> data,
                         @Nullable String deeplink,
                         @Nullable String iconUrl);

  void notifyUserDataOnly(String username,
                                 @Nullable Map<String, String> data,
                                 @Nullable String topicHint);
}
