package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.UserEntity;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Nullable
    UserEntity findByUsername(String username);

    List<UserEntity> findByUsernameNot(String username);
}
