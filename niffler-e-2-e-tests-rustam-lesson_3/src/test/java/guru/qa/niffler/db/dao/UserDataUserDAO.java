package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.UserDataEntity;
import guru.qa.niffler.db.model.UserEntity;
import java.util.UUID;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserDataUserDAO {

  static UserDataUserDAO getImpl() {
    if ("hibernate".equals(System.getProperty("db.impl"))) {
      return new AuthUserDAOHibernate();
    } else if ("spring".equals(System.getProperty("db.impl"))) {
      return new AuthUserDAOSpringJdbc();
    } else {
      return new AuthUserDAOJdbc();
    }
  }

  int createUserInUserData(UserEntity user);

  UserDataEntity getUserdataInUserData(String username);

  UserDataEntity updateUserInUserData(UserDataEntity userData);

  void deleteUserByIdInUserData(UUID userId);
  void deleteUserByUsernameInUserData(String username);
}
