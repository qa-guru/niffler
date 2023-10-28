package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.dao.impl.AuthUserDAOJdbc;
import guru.qa.niffler.db.dao.impl.UserDataUserDAOHibernate;
import guru.qa.niffler.db.entity.userdata.UserDataEntity;
import java.util.UUID;

public interface UserDataUserDAO {

  static UserDataUserDAO getImpl() {
    if ("hibernate".equals(System.getProperty("db.impl"))) {
//      return new AuthUserDAOHibernate();
      return null;
    } else if ("spring".equals(System.getProperty("db.impl"))) {
      return new AuthUserDAOJdbc();
    } else {
//      return new AuthUserDAOJdbc();
//      return new AuthUserDAOSpringJdbc();
      return new UserDataUserDAOHibernate();
    }
  }

  UserDataEntity createUserInUserData(UserDataEntity userData);

  UserDataEntity getUserdataInUserData(UserDataEntity userData);

  UserDataEntity getUserdataInUserDataByUserName(String username);

  UserDataEntity updateUserInUserData(UserDataEntity userData);

  void deleteUserByIdInUserData(UUID userId);

  void deleteUserByUsernameInUserData(UserDataEntity userData);
}
