package niffler.data;

import niffler.data.model.Users;

public interface UsersDAO extends DAO {
    int addUser(Users users);
    void updateUser(Users user);
    void remove(Users user);
    Users getByUsername(String username);
}
