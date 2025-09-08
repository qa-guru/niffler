package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.PushTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface PushTokenRepository extends JpaRepository<PushTokenEntity, UUID> {
  @Nonnull
  Optional<PushTokenEntity> findByToken(String token);

  @Nonnull
  @Query("select t.token from PushTokenEntity t where t.user.username = :username and t.active = true")
  List<String> findActiveTokensByUsername(@Param("username") String username);
}
