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
  public UserEntity updateUser(UserEntity user) {
    return null;
  }

  @Override
  public void deleteUserById(UUID userId) {

  }

  @Override
  public int createUserInUserData(UserEntity user) {
    return 0;
  }

  @Override
  public UserDataEntity getUserdataInUserData(String username) {
    return null;
  }

  @Override
  public UserDataEntity updateUserInUserData(UserDataEntity userData) {
    return null;
  }

  @Override
  public void deleteUserByIdInUserData(UUID userId) {

  }

  @Override
  public void deleteUserByUsernameInUserData(String username) {

  }
}
