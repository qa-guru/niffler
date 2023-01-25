package niffler.data.repository;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import niffler.data.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

    @Nullable
    CategoryEntity findByDescription(@Nonnull String description);

}
