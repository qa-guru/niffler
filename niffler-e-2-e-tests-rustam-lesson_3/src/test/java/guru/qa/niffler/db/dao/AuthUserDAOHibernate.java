package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.UserDataEntity;
import guru.qa.niffler.db.model.UserEntity;
import java.util.UUID;

public class AuthUserDAOHibernate implements AuthUserDAO, UserDataUserDAO {

  @Override
  public int createUser(UserEntity user) {
    return 0;
  }

  @Override
  public UserEntity getUserById(UUID userId) {
    return null;
  }

  @Override
  public void updateUser(UserEntity user) {

  }

  @Override
  public void deleteUserById(UUID userId) {

  }

  @Override
  public int createUserInUserData(UserEntity user) {
    return 0;
  }

  @Override
  public int readUserInUserData(UserEntity user) {
    return 0;
  }

  @Override
  public void updateUserInUserData(UserDataEntity userData) {

  }

  @Override
  public void deleteUserByIdInUserData(UserEntity user) {

  }
}
