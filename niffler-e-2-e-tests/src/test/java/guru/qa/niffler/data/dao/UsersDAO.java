package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.UsersEntity;

public interface UsersDAO extends DAO {
    int addUser(UsersEntity users);

    void updateUser(UsersEntity user);

    void remove(UsersEntity user);

    UsersEntity getByUsername(String username);
}
