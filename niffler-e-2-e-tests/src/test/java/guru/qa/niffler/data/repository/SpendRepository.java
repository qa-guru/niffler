package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.Optional;
import java.util.UUID;

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

    SpendEntity createSpend(SpendEntity spend);

    Optional<SpendEntity> findSpendById(UUID id);

    CategoryEntity createCategory(CategoryEntity category);

    Optional<CategoryEntity> findCategoryById(UUID id);

    Optional<CategoryEntity> findUserCategoryByName(String username, String category);
}
