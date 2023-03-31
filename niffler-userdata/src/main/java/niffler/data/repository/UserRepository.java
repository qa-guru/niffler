package niffler.data.repository;

import jakarta.annotation.Nullable;
import niffler.data.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Nullable
    UserEntity findByUsername(String username);

    List<UserEntity> findByUsernameNot(String username);
}
