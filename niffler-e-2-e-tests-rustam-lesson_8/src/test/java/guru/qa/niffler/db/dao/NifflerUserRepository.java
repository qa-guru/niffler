package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.dao.impl.AuthUserDAOHibernate;
import guru.qa.niffler.db.dao.impl.AuthUserDAOJdbc;
import guru.qa.niffler.db.dao.impl.UserDataUserDAOHibernate;
import guru.qa.niffler.db.entity.auth.UserEntity;
import guru.qa.niffler.db.entity.userdata.CurrencyValues;
import guru.qa.niffler.db.entity.userdata.UserDataEntity;

public class NifflerUserRepository {

  private final AuthUserDAO authUserDAO = new AuthUserDAOHibernate();
  private final UserDataUserDAO userdataUserDAO = new UserDataUserDAOHibernate();

  public void createUserForTest(UserEntity user) {
    authUserDAO.createUser(user);
    userdataUserDAO.createUserInUserData(fromAuthUserEntity(user));
  }

  public void removeAfterTest(UserEntity user) {
    UserDataEntity userData = userdataUserDAO.getUserdataInUserDataByUserName(user.getUsername());
    userdataUserDAO.deleteUserByUsernameInUserData(userData);
    authUserDAO.deleteUser(user);
  }

  public UserDataEntity fromAuthUserEntity(UserEntity user) {
    UserDataEntity userData = new UserDataEntity();
    userData.setUsername(user.getUsername());
    userData.setCurrency(CurrencyValues.USD);
    userData.setFirstname("updated_firstname_2");
    userData.setSurname("updated_surname_2");
    userData.setPhoto("photos/photo2.jpeg".getBytes());
    return userData;
  }
}
