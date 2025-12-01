package guru.qa.niffler.service;

import guru.qa.niffler.model.FcmTokenJson;
import guru.qa.niffler.model.UserJson;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface UserDataClient {

  @Nonnull
  UserJson currentUser(String username);

  @Nonnull
  UserJson updateUserInfo(@Nonnull UserJson user);

  @Nonnull
  List<UserJson> allUsers(String username,
                          @Nullable String searchQuery);

  @Nonnull
  Page<UserJson> allUsersV2(String username,
                            Pageable pageable,
                            @Nullable String searchQuery);

  @Nonnull
  PagedModel<UserJson> allUsersV3(String username,
                                  Pageable pageable,
                                  @Nullable String searchQuery);

  @Nonnull
  List<UserJson> friends(String username,
                         @Nullable String searchQuery);

  @Nonnull
  Page<UserJson> friendsV2(String username,
                           Pageable pageable,
                           @Nullable String searchQuery);

  @Nonnull
  PagedModel<UserJson> friendsV3(String username,
                                 Pageable pageable,
                                 @Nullable String searchQuery);

  @Nonnull
  UserJson sendInvitation(String username,
                          String targetUsername);

  @Nonnull
  UserJson acceptInvitation(String username,
                            String targetUsername);

  @Nonnull
  UserJson declineInvitation(String username,
                             String targetUsername);

  void removeFriend(String username,
                    String targetUsername);

  void registerToken(FcmTokenJson fcmTokenJson);
}
