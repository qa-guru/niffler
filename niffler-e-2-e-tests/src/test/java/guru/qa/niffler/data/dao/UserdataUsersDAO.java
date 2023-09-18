package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.ud.UserEntity;

import java.util.Optional;

public interface UserdataUsersDAO {
    void createUser(UserEntity user);

    UserEntity updateUser(UserEntity user);

    void deleteUser(UserEntity user);

    Optional<UserEntity> findUserByUsername(String username);
}
