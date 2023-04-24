package niffler.db.dao;

import niffler.db.entity.UserEntity;

public interface NifflerUsersDAO {

  int createUser(UserEntity user);

  String getUserId(String userName);

}
