package niffler.test;

import niffler.data.dao.PostgresSpringJdbcUsersAuthRepository;
import niffler.data.dao.UsersAuthRepository;
import niffler.data.entity.UserAuthEntity;
import org.junit.jupiter.api.Test;

public class UserAuthTest {

    UsersAuthRepository usersAuthRepository = new PostgresSpringJdbcUsersAuthRepository();

    UserAuthEntity userAuthEntityWrite = UserAuthEntity.builder()
            .username("UserWrite")
            .password("password-write")
            .build();

    UserAuthEntity userAuthEntityRead = UserAuthEntity.builder()
            .username("UserRead")
            .password("password-read")
            .build();

    @Test
    void createUsers() {
        UserAuthEntity createdWrite = usersAuthRepository.createUserWithReadAndWriteAuthority(userAuthEntityWrite);
        UserAuthEntity createdRead = usersAuthRepository.createUserWithReadAuthority(userAuthEntityRead);

        System.out.println(createdWrite);
        System.out.println(createdRead);
    }
}
