package guru.qa.niffler.service;

import guru.qa.niffler.data.SpendEntity;
import guru.qa.niffler.data.projection.SumByCategoryAggregate;
import guru.qa.niffler.data.projection.SumByCategoryInUserCurrency;
import guru.qa.niffler.data.projection.SumByCategoryInfo;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.StatisticByCategoryJson;
import guru.qa.niffler.model.StatisticJson;
import guru.qa.niffler.model.StatisticV2Json;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class StatService {

  private final SpendService spendService;
  private final CategoryService categoryService;
  private final GrpcCurrencyClient grpcCurrencyClient;

  @Autowired
  public StatService(SpendService spendService, CategoryService categoryService, GrpcCurrencyClient grpcCurrencyClient) {
    this.spendService = spendService;
    this.categoryService = categoryService;
    this.grpcCurrencyClient = grpcCurrencyClient;
  }

  @Transactional(readOnly = true)
  public @Nonnull
  StatisticV2Json getV2Statistic(@Nonnull String username,
                                 @Nonnull CurrencyValues statCurrency,
                                 @Nullable CurrencyValues filterCurrency,
                                 @Nullable Date dateFrom,
                                 @Nullable Date dateTo) {
    List<SumByCategoryInfo> result = new ArrayList<>();
    for (List<SumByCategoryInfo> delegates : spendService.getSumByCategories(username, dateFrom, dateTo)
        .stream().parallel()
        .filter(sumByCategory -> filterCurrency == null || sumByCategory.currency() == filterCurrency)
        .map(sumByCategory -> mapToUserCurrency(statCurrency, sumByCategory))
        .collect(toCategoriesMap())
        .values()) {
      SumByCategoryInfo sumByCategoryInfo = new SumByCategoryAggregate(delegates);
      result.add(sumByCategoryInfo);
    }
    result.sort(Comparator.comparing((SumByCategoryInfo p) -> !p.categoryName().equals("Archived"))
        .thenComparing(SumByCategoryInfo::sum).reversed());

    return new StatisticV2Json(
        getTotalSumm(result),
        statCurrency,
        result
    );
  }

  @Transactional(readOnly = true)
  public @Nonnull
  List<StatisticJson> getStatistic(@Nonnull String username,
                                   @Nonnull CurrencyValues statCurrency,
                                   @Nullable CurrencyValues filterCurrency,
                                   @Nullable Date dateFrom,
                                   @Nullable Date dateTo) {
    List<SpendEntity> spendEntities = spendService.getSpendsEntityForUser(username, filterCurrency, dateFrom, dateTo);
    List<StatisticJson> result = new ArrayList<>();

    CurrencyValues[] desiredCurrenciesInResponse = resolveDesiredCurrenciesInStatistic(filterCurrency);

    for (CurrencyValues statisticCurrency : desiredCurrenciesInResponse) {
      StatisticJson enriched = calculateStatistic(statisticCurrency, username, statCurrency, spendEntities, dateTo);
      result.add(enriched);
    }
    return result;
  }

  @Nonnull
  StatisticJson calculateStatistic(@Nonnull CurrencyValues statisticCurrency,
                                   @Nonnull String username,
                                   @Nonnull CurrencyValues userCurrency,
                                   @Nonnull List<SpendEntity> spendEntities,
                                   @Nullable Date dateTo) {
    StatisticJson statistic = createDefaultStatisticJson(statisticCurrency, userCurrency, dateTo);
    List<SpendEntity> sortedSpends = spendEntities.stream()
        .filter(se -> se.getCurrency() == statisticCurrency)
        .sorted(Comparator.comparing(SpendEntity::getSpendDate))
        .toList();

    statistic = calculateStatistic(statistic, statisticCurrency, userCurrency, sortedSpends);
    Map<String, List<SpendJson>> spendsByCategory = bindSpendsToCategories(sortedSpends);

    List<StatisticByCategoryJson> sbcjResult = new ArrayList<>();
    for (Map.Entry<String, List<SpendJson>> entry : spendsByCategory.entrySet()) {
      double total = entry.getValue().stream()
          .map(SpendJson::amount)
          .map(BigDecimal::valueOf)
          .reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();

      double totalInUserDefaultCurrency = grpcCurrencyClient.calculate(
          total,
          statisticCurrency,
          userCurrency
      ).doubleValue();

      sbcjResult.add(new StatisticByCategoryJson(
          entry.getKey(),
          total,
          totalInUserDefaultCurrency,
          entry.getValue()
      ));
    }

    categoryService.getAllCategories(username, true).stream()
        .filter(c -> !spendsByCategory.containsKey(c.name()))
        .map(c -> new StatisticByCategoryJson(
            c.name(),
            0.0,
            0.0,
            Collections.emptyList()
        ))
        .forEach(sbcjResult::add);

    sbcjResult.sort(Comparator.comparing(StatisticByCategoryJson::category));
    statistic.categoryStatistics().addAll(sbcjResult);
    return statistic;
  }

  @Nonnull
  Function<SpendEntity, StatisticJson> enrichStatisticDateFromByFirstStreamElement(@Nonnull StatisticJson statistic) {
    return se -> (statistic.dateFrom() == null)
        ?
        new StatisticJson(
            se.getSpendDate(),
            statistic.dateTo(),
            statistic.currency(),
            statistic.total(),
            statistic.userDefaultCurrency(),
            statistic.totalInUserDefaultCurrency(),
            statistic.categoryStatistics()
        )
        :
        statistic;
  }

  @Nonnull
  Function<SpendEntity, StatisticJson> enrichStatisticTotalAmountByAllStreamElements(@Nonnull StatisticJson statistic) {
    return se -> new StatisticJson(
        statistic.dateFrom(),
        statistic.dateTo(),
        statistic.currency(),
        BigDecimal.valueOf(statistic.total())
            .add(BigDecimal.valueOf(se.getAmount()))
            .doubleValue(),
        statistic.userDefaultCurrency(),
        statistic.totalInUserDefaultCurrency(),
        statistic.categoryStatistics()
    );
  }

  @Nonnull
  Function<SpendEntity, StatisticJson> enrichStatisticTotalInUserCurrencyByAllStreamElements(@Nonnull StatisticJson statistic,
                                                                                             @Nonnull CurrencyValues statisticCurrency,
                                                                                             @Nonnull CurrencyValues userCurrency) {
    return se -> new StatisticJson(
        statistic.dateFrom(),
        statistic.dateTo(),
        statistic.currency(),
        statistic.total(),
        statistic.userDefaultCurrency(),
        (userCurrency != statisticCurrency)
            ? BigDecimal.valueOf(statistic.totalInUserDefaultCurrency())
            .add(
                grpcCurrencyClient.calculate(
                    se.getAmount(), se.getCurrency(), userCurrency
                )
            ).doubleValue()
            : statistic.total(),
        statistic.categoryStatistics()
    );
  }

  @Nonnull
  Map<String, List<SpendJson>> bindSpendsToCategories(@Nonnull List<SpendEntity> sortedSpends) {
    return sortedSpends.stream().map(SpendJson::fromEntity)
        .collect(Collectors.groupingBy(
            (SpendJson sj) -> {
              CategoryJson ce = sj.category();
              return ce.archived()
                  ? CategoryService.ARCHIVED_CATEGORY_NAME
                  : ce.name();
            },
            HashMap::new,
            Collectors.toCollection(ArrayList::new)
        ));
  }

  @Nonnull
  StatisticJson calculateStatistic(@Nonnull StatisticJson statistic,
                                   @Nonnull CurrencyValues statisticCurrency,
                                   @Nonnull CurrencyValues userCurrency,
                                   @Nonnull List<SpendEntity> sortedSpends) {
    for (SpendEntity spend : sortedSpends) {
      statistic = enrichStatisticTotalInUserCurrencyByAllStreamElements(
          enrichStatisticTotalAmountByAllStreamElements(
              enrichStatisticDateFromByFirstStreamElement(
                  statistic
              ).apply(spend)
          ).apply(spend), statisticCurrency, userCurrency
      ).apply(spend);
    }
    return statistic;
  }

  @Nonnull
  StatisticJson createDefaultStatisticJson(@Nonnull CurrencyValues statisticCurrency,
                                           @Nonnull CurrencyValues userCurrency,
                                           @Nullable Date dateTo) {
    return new StatisticJson(
        null,
        dateTo,
        statisticCurrency,
        0.0,
        userCurrency,
        0.0,
        new ArrayList<>()
    );
  }

  @Nonnull
  CurrencyValues[] resolveDesiredCurrenciesInStatistic(@Nullable CurrencyValues filterCurrency) {
    return filterCurrency != null
        ? new CurrencyValues[]{filterCurrency}
        : CurrencyValues.values();
  }

  @Nonnull
  Collector<SumByCategoryInfo, ?, HashMap<String, List<SumByCategoryInfo>>> toCategoriesMap() {
    return Collectors.groupingBy(
        SumByCategoryInfo::categoryName,
        HashMap::new,
        Collectors.toCollection(ArrayList::new)
    );
  }

  @Nonnull
  SumByCategoryInfo mapToUserCurrency(@Nonnull CurrencyValues userCurrency,
                                      @Nonnull SumByCategoryInfo sumByCategory) {
    if (sumByCategory.currency() != userCurrency) {
      return new SumByCategoryInUserCurrency(
          sumByCategory,
          userCurrency,
          grpcCurrencyClient.calculate(
              sumByCategory.sum(),
              sumByCategory.currency(),
              userCurrency
          ).setScale(2, RoundingMode.HALF_UP).doubleValue()
      );
    } else {
      return sumByCategory;
    }
  }

  double getTotalSumm(@Nonnull List<SumByCategoryInfo> result) {
    return result.stream()
        .map(SumByCategoryInfo::sum)
        .map(BigDecimal::valueOf)
        .reduce(BigDecimal.ZERO, BigDecimal::add)
        .setScale(2, RoundingMode.HALF_UP)
        .doubleValue();
  }
}
