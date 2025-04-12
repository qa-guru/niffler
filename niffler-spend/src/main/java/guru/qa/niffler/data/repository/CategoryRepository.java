package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.CategoryEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

  @Nonnull
  Optional<CategoryEntity> findByUsernameAndName(String username, String category);

  @Nonnull
  Optional<CategoryEntity> findByUsernameAndId(String username, UUID id);

  @Nonnull
  @Query("SELECT c FROM CategoryEntity c WHERE c.username = :username ORDER BY c.archived ASC, c.name ASC")
  List<CategoryEntity> findAllByUsernameOrderByName(String username);

  long countByUsernameAndArchived(String username, boolean archived);
}
