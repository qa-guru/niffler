package guru.qa.niffler.service;

import guru.qa.niffler.data.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.StatisticByCategoryJson;
import guru.qa.niffler.model.StatisticJson;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
    List<StatisticJson> getStatistic(@Nonnull String username,
                                     @Nonnull CurrencyValues userCurrency,
                                     @Nullable CurrencyValues filterCurrency,
                                     @Nullable Date dateFrom,
                                     @Nullable Date dateTo) {
        List<SpendEntity> spendEntities = spendService.getSpendsEntityForUser(username, filterCurrency, dateFrom, dateTo);
        List<StatisticJson> result = new ArrayList<>();

        CurrencyValues[] desiredCurrenciesInResponse = resolveDesiredCurrenciesInStatistic(filterCurrency);

        for (CurrencyValues statisticCurrency : desiredCurrenciesInResponse) {
            StatisticJson enriched = calculateStatistic(statisticCurrency, username, userCurrency, spendEntities, dateTo);
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

        categoryService.getAllCategories(username).stream()
                .filter(c -> !spendsByCategory.containsKey(c.category()))
                .map(c -> new StatisticByCategoryJson(
                        c.category(),
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
                        SpendJson::category,
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
}
