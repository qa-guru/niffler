package niffler.data.dao;

import niffler.data.entity.UserAuthEntity;

public interface UsersAuthRepository extends DAO {
    UserAuthEntity createUserWithReadAuthority(UserAuthEntity userAuthEntity);

    UserAuthEntity createUserWithReadAndWriteAuthority(UserAuthEntity userAuthEntity);

    UserAuthEntity getByUsername(String username);
}
