package niffler.data;

import niffler.data.model.UsersEntity;

public interface UsersDAO extends DAO {
    int addUser(UsersEntity users);

    void updateUser(UsersEntity user);

    void remove(UsersEntity user);

    UsersEntity getByUsername(String username);
}
