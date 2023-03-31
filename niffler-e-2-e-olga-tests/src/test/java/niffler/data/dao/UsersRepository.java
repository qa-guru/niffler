package niffler.data.dao;

import niffler.data.entity.UsersEntity;

public interface UsersRepository extends DAO {
    UsersEntity getByUsername(String username);

    void addUser(UsersEntity usersEntity);

    void updateUser(UsersEntity usersEntity);

    void removeUser(UsersEntity usersEntity);

}