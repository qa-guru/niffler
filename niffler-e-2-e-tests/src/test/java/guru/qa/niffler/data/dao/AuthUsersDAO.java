package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

public interface AuthUsersDAO {
    PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    void createUser(AuthUserEntity user);

    AuthUserEntity updateUser(AuthUserEntity user);

    void deleteUser(AuthUserEntity user);

    Optional<AuthUserEntity> findUserById(UUID userId);

    Optional<AuthUserEntity> findUserByUsername(String username);
}
