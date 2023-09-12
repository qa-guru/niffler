package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.ud.UserEntity;

public interface UserRepository {

    static UserRepository getRepository() {
        if ("jpa".equals(System.getProperty("repository", null))) {
            return new UserRepositoryHibernate();
        } else if ("jdbc".equals(System.getProperty("repository", null))) {
            return new UserRepositoryJdbc();
        } else {
            return new UserRepositorySpringJdbc();
        }
    }

    void createUserForTest(AuthUserEntity user);

    AuthUserEntity getTestUserFromAuth(String username);

    UserEntity getTestUserFromUserdata(String username);

    void updateUserForTest(AuthUserEntity user);

    void updateUserForTest(UserEntity user);

    void removeAfterTest(AuthUserEntity user);
}
