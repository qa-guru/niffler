package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.CategoryEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

    @Nonnull
    Optional<CategoryEntity> findByUsernameAndCategory(@Nonnull String username, @Nonnull String category);

    @Nonnull
    List<CategoryEntity> findAllByUsernameOrderByCategory(@Nonnull String username);

    long countByUsername(@Nonnull String username);
}
