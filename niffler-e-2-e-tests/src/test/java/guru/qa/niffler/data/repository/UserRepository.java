package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.util.Optional;
import java.util.UUID;

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

    AuthUserEntity createInAuth(AuthUserEntity user);

    Optional<AuthUserEntity> findByIdInAuth(UUID id);

    UserEntity createInUserdata(UserEntity user);

    Optional<UserEntity> findByIdInUserdata(UUID id);

    void updateInAuth(AuthUserEntity user);

    void updateInUserdata(UserEntity user);

    void deleteInAuth(AuthUserEntity user);

    void deleteInUserdata(UserEntity user);
}
