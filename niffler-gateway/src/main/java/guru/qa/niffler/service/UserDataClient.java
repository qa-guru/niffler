package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import java.util.List;

public interface UserDataClient {

  @Nonnull
  UserJson currentUser(@Nonnull String username);

  @Nonnull
  UserJson updateUserInfo(@Nonnull UserJson user);

  @Nonnull
  List<UserJson> allUsers(@Nonnull String username,
                          @Nullable String searchQuery);

  @Nonnull
  Page<UserJson> allUsersV2(@Nonnull String username,
                            @Nonnull Pageable pageable,
                            @Nullable String searchQuery);

  @Nonnull
  PagedModel<UserJson> allUsersV3(@Nonnull String username,
                                  @Nonnull Pageable pageable,
                                  @Nullable String searchQuery);

  @Nonnull
  List<UserJson> friends(@Nonnull String username,
                         @Nullable String searchQuery);

  @Nonnull
  Page<UserJson> friendsV2(@Nonnull String username,
                           @Nonnull Pageable pageable,
                           @Nullable String searchQuery);

  @Nonnull
  PagedModel<UserJson> friendsV3(@Nonnull String username,
                                 @Nonnull Pageable pageable,
                                 @Nullable String searchQuery);

  @Nonnull
  UserJson sendInvitation(@Nonnull String username,
                          @Nonnull String targetUsername);

  @Nonnull
  UserJson acceptInvitation(@Nonnull String username,
                            @Nonnull String targetUsername);

  @Nonnull
  UserJson declineInvitation(@Nonnull String username,
                             @Nonnull String targetUsername);

  void removeFriend(@Nonnull String username,
                    @Nonnull String targetUsername);
}
