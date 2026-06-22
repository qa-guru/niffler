package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UserRepository {

  static UserRepository getInstance() {
    if ("spring".equals(System.getProperty("repository", null))) {
      return new UserRepositorySpringJdbc();
    } else if ("jdbc".equals(System.getProperty("repository", null))) {
      return new UserRepositoryJdbc();
    } else {
      return new UserRepositoryHibernate();
    }
  }

  @Nonnull
  AuthUserEntity createInAuth(AuthUserEntity user);

  @Nonnull
  Optional<AuthUserEntity> findByIdInAuth(UUID id);

  @Nonnull
  UserEntity createInUserdata(UserEntity user);

  @Nonnull
  Optional<UserEntity> findByIdInUserdata(UUID id);

  void updateInAuth(AuthUserEntity user);

  void updateInUserdata(UserEntity user);

  void deleteInAuth(AuthUserEntity user);

  void deleteInUserdata(UserEntity user);
}
