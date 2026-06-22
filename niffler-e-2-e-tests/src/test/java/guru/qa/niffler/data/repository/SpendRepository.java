package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface SpendRepository {

  static SpendRepository getInstance() {
    if ("spring".equals(System.getProperty("repository", null))) {
      return new SpendRepositorySpringJdbc();
    } else if ("jdbc".equals(System.getProperty("repository", null))) {
      return new SpendRepositoryJdbc();
    } else {
      return new SpendRepositoryHibernate();
    }
  }

  @Nonnull
  SpendEntity createSpend(SpendEntity spend);

  @Nonnull
  Optional<SpendEntity> findSpendById(UUID id);

  @Nonnull
  CategoryEntity createCategory(CategoryEntity category);

  @Nonnull
  Optional<CategoryEntity> findCategoryById(UUID id);

  @Nonnull
  Optional<CategoryEntity> findUserCategoryByName(String username, String category);
}
