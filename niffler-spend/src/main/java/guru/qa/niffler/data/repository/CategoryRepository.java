package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.CategoryEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

  @Nonnull
  Optional<CategoryEntity> findByUsernameAndName(@Nonnull String username, @Nonnull String category);

  @Nonnull
  Optional<CategoryEntity> findByUsernameAndId(@Nonnull String username, @Nonnull UUID id);

  @Nonnull
  @Query("SELECT c FROM CategoryEntity c WHERE c.username = :username ORDER BY c.archived ASC, c.name ASC")
  List<CategoryEntity> findAllByUsernameOrderByName(@Nonnull String username);

  long countByUsernameAndArchived(@Nonnull String username, boolean archived);
}
