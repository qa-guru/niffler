package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.DataBase.SPEND;
import static guru.qa.niffler.data.jpa.EmfContext.entityManager;

public class SpendRepositoryHibernate implements SpendRepository {

    private final EntityManager spendEm = entityManager(SPEND);

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        spendEm.persist(spend);
        return spend;
    }

    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {
        return Optional.ofNullable(
                spendEm.find(SpendEntity.class, id)
        );
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        spendEm.persist(category);
        return category;
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return Optional.ofNullable(
                spendEm.find(CategoryEntity.class, id)
        );
    }

    @Override
    public Optional<CategoryEntity> findUserCategoryByName(String username,
                                                           String category) {
        try {
            return Optional.of(spendEm.createQuery("select c from CategoryEntity c where c.category=:category and c.username=:username", CategoryEntity.class)
                    .setParameter("category", category)
                    .setParameter("username", username)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
