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
  List<SpendEntity> findAllByUsernameAndSpendDateGreaterThanEqualAndSpendDateLessThanEqualOrderBySpendDateDesc(
      @Nonnull String username,
      @Nonnull Date dateFrom,
      @Nonnull Date dateTo
  );

  @Nonnull
  List<SpendEntity> findAllByUsernameAndCurrencyAndSpendDateGreaterThanEqualAndSpendDateLessThanEqualOrderBySpendDateDesc(
      @Nonnull String username,
      @Nonnull CurrencyValues currency,
      @Nonnull Date dateFrom,
      @Nonnull Date dateTo
  );

  @Nonnull
  @Query(
      "select s from SpendEntity s left join CategoryEntity c on s.category = c " +
          "where s.username = :username " +
          "and s.spendDate >= :dateFrom and s.spendDate <= :dateTo " +
          "order by s.spendDate desc "
  )
  Page<SpendEntity> findAll(
      @Nonnull String username,
      @Nonnull Date dateFrom,
      @Nonnull Date dateTo,
      @Nonnull Pageable pageable
  );

  @Nonnull
  @Query(
      "select s from SpendEntity s left join CategoryEntity c on s.category = c " +
          "where s.username = :username " +
          "and (lower(s.description) like lower(concat('%', :searchQuery, '%')) or lower(c.name) like lower(concat('%', :searchQuery, '%')))" +
          "and s.spendDate >= :dateFrom and s.spendDate <= :dateTo " +
          "order by s.spendDate desc "
  )
  Page<SpendEntity> findAll(
      @Nonnull String username,
      @Nonnull Date dateFrom,
      @Nonnull Date dateTo,
      @Nonnull String searchQuery,
      @Nonnull Pageable pageable
  );

  @Nonnull
  @Query(
      "select s from SpendEntity s left join CategoryEntity c on s.category = c " +
          "where s.username = :username " +
          "and s.currency = :currency " +
          "and s.spendDate >= :dateFrom and s.spendDate <= :dateTo " +
          "order by s.spendDate desc "
  )
  Page<SpendEntity> findAll(
      @Nonnull String username,
      @Nonnull CurrencyValues currency,
      @Nonnull Date dateFrom,
      @Nonnull Date dateTo,
      @Nonnull Pageable pageable
  );

  @Nonnull
  @Query(
      "select s from SpendEntity s left join CategoryEntity c on s.category = c " +
          "where s.username = :username " +
          "and s.currency = :currency " +
          "and (lower(s.description) like lower(concat('%', :searchQuery, '%')) or lower(c.name) like lower(concat('%', :searchQuery, '%')))" +
          "and s.spendDate >= :dateFrom and s.spendDate <= :dateTo " +
          "order by s.spendDate desc "
  )
  Page<SpendEntity> findAll(
      @Nonnull String username,
      @Nonnull CurrencyValues currency,
      @Nonnull Date dateFrom,
      @Nonnull Date dateTo,
      @Nonnull String searchQuery,
      @Nonnull Pageable pageable
  );

  void deleteByUsernameAndIdIn(@Nonnull String username, @Nonnull List<UUID> ids);
}
