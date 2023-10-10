package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.userdata.UserDataEntity;
import guru.qa.niffler.db.model.auth.UserEntity;
import java.util.UUID;

public interface UserDataUserDAO {

  static UserDataUserDAO getImpl() {
    if ("hibernate".equals(System.getProperty("db.impl"))) {
//      return new AuthUserDAOHibernate();
      return null;
    } else if ("spring".equals(System.getProperty("db.impl"))) {
      return new AuthUserDAOJdbc();
    } else {
      return new UserDataUserDAOHibernate();
    }
  }

  UserDataEntity createUserInUserData(UserDataEntity userData);

  UserDataEntity getUserdataInUserData(UserDataEntity userData);

  UserDataEntity updateUserInUserData(UserDataEntity userData);

  void deleteUserByIdInUserData(UUID userId);

  void deleteUserByUsernameInUserData(UserDataEntity userData);
}
