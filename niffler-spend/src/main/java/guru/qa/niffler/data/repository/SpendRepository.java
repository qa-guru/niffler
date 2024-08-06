package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.SpendEntity;
import guru.qa.niffler.data.projection.SumByCategory;
import guru.qa.niffler.model.CurrencyValues;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendRepository extends JpaRepository<SpendEntity, UUID> {

    @Nonnull
    Optional<SpendEntity> findByIdAndUsername(@Nonnull UUID id, @Nonnull String username);

    @Query(
            "SELECT new guru.qa.niffler.data.projection.SumByCategory('Archived', s.currency, ROUND(SUM(s.amount), 2), MIN(s.spendDate), MAX(s.spendDate))  FROM SpendEntity s join CategoryEntity c on s.category = c " +
                    "where s.username = :username and s.category.archived = true and s.spendDate >= :dateFrom and s.spendDate <= :dateTo " +
                    "group by s.currency"
    )
    List<SumByCategory> statisticByArchivedCategory(
            @Nonnull String username,
            @Nonnull Date dateFrom,
            @Nonnull Date dateTo
    );

    @Query(
            "SELECT new guru.qa.niffler.data.projection.SumByCategory(c.name, s.currency, ROUND(SUM(s.amount), 2), MIN(s.spendDate), MAX(s.spendDate))  from SpendEntity s join CategoryEntity c on s.category = c " +
                    "where s.username = :username and s.category.archived = false and s.spendDate >= :dateFrom and s.spendDate <= :dateTo " +
                    "group by c.name, s.currency"
    )
    List<SumByCategory> statisticByCategory(
            @Nonnull String username,
            @Nonnull Date dateFrom,
            @Nonnull Date dateTo
    );

    @Nonnull
    List<SpendEntity> findAllByUsername(@Nonnull String username);

    @Nonnull
    Page<SpendEntity> findAllByUsername(
            @Nonnull String username,
            @Nonnull Pageable pageable
    );

    @Nonnull
    List<SpendEntity> findAllByUsernameAndSpendDateGreaterThanEqual(
            @Nonnull String username,
            @Nonnull Date dateFrom
    );

    @Nonnull
    List<SpendEntity> findAllByUsernameAndCurrencyAndSpendDateGreaterThanEqual(
            @Nonnull String username,
            @Nonnull CurrencyValues currency,
            @Nonnull Date dateFrom
    );

    @Nonnull
    Page<SpendEntity> findAllByUsernameAndSpendDateGreaterThanEqual(
            @Nonnull String username,
            @Nonnull Date dateFrom,
            @Nonnull Pageable pageable
    );

    @Nonnull
    Page<SpendEntity> findAllByUsernameAndCurrencyAndSpendDateGreaterThanEqual(
            @Nonnull String username,
            @Nonnull CurrencyValues currency,
            @Nonnull Date dateFrom,
            @Nonnull Pageable pageable
    );

    @Nonnull
    List<SpendEntity> findAllByUsernameAndSpendDateLessThanEqual(
            @Nonnull String username,
            @Nonnull Date dateTo
    );

    @Nonnull
    List<SpendEntity> findAllByUsernameAndCurrencyAndSpendDateLessThanEqual(
            @Nonnull String username,
            @Nonnull CurrencyValues currency,
            @Nonnull Date dateTo
    );

    @Nonnull
    Page<SpendEntity> findAllByUsernameAndSpendDateLessThanEqual(
            @Nonnull String username,
            @Nonnull Date dateTo,
            @Nonnull Pageable pageable
    );

    @Nonnull
    Page<SpendEntity> findAllByUsernameAndCurrencyAndSpendDateLessThanEqual(
            @Nonnull String username,
            @Nonnull CurrencyValues currency,
            @Nonnull Date dateTo,
            @Nonnull Pageable pageable
    );

    @Nonnull
    List<SpendEntity> findAllByUsernameAndSpendDateGreaterThanEqualAndSpendDateLessThanEqual(
            @Nonnull String username,
            @Nonnull Date dateFrom,
            @Nonnull Date dateTo
    );

    @Nonnull
    List<SpendEntity> findAllByUsernameAndCurrencyAndSpendDateGreaterThanEqualAndSpendDateLessThanEqual(
            @Nonnull String username,
            @Nonnull CurrencyValues currency,
            @Nonnull Date dateFrom,
            @Nonnull Date dateTo
    );

    @Nonnull
    Page<SpendEntity> findAllByUsernameAndSpendDateGreaterThanEqualAndSpendDateLessThanEqual(
            @Nonnull String username,
            @Nonnull Date dateFrom,
            @Nonnull Date dateTo,
            @Nonnull Pageable pageable
    );

    @Nonnull
    Page<SpendEntity> findAllByUsernameAndCurrencyAndSpendDateGreaterThanEqualAndSpendDateLessThanEqual(
            @Nonnull String username,
            @Nonnull CurrencyValues currency,
            @Nonnull Date dateFrom,
            @Nonnull Date dateTo,
            @Nonnull Pageable pageable
    );

    void deleteByUsernameAndIdIn(@Nonnull String username, @Nonnull List<UUID> ids);
}
