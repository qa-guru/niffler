package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.CategoryEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

    @Nullable
    CategoryEntity findByUsernameAndCategory(@Nonnull String username, @Nonnull String category);

    @Nonnull
    List<CategoryEntity> findAllByUsername(@Nonnull String username);
}
