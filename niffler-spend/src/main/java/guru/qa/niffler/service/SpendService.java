package guru.qa.niffler.service;

import guru.qa.niffler.data.CategoryEntity;
import guru.qa.niffler.data.SpendEntity;
import guru.qa.niffler.data.repository.CategoryRepository;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.StatisticByCategoryJson;
import guru.qa.niffler.model.StatisticJson;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SpendService {

    private final SpendRepository spendRepository;
    private final CategoryRepository categoryRepository;
    private final GrpcCurrencyClient grpcCurrencyClient;

    @Autowired
    public SpendService(SpendRepository spendRepository, CategoryRepository categoryRepository, GrpcCurrencyClient grpcCurrencyClient) {
        this.spendRepository = spendRepository;
        this.categoryRepository = categoryRepository;
        this.grpcCurrencyClient = grpcCurrencyClient;
    }

    @Transactional
    public @Nonnull
    SpendJson saveSpendForUser(@Nonnull SpendJson spend) {
        final String username = spend.username();
        final String category = spend.category();

        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setUsername(username);
        spendEntity.setSpendDate(spend.spendDate());
        spendEntity.setCurrency(spend.currency());
        spendEntity.setDescription(spend.description());
        spendEntity.setAmount(spend.amount());

        CategoryEntity categoryEntity = categoryRepository.findAllByUsername(username)
                .stream()
                .filter(c -> c.getCategory().equals(category))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Can`t find category by given name: " + category));

        spendEntity.setCategory(categoryEntity);
        return SpendJson.fromEntity(spendRepository.save(spendEntity));
    }

    @Transactional
    public @Nonnull
    SpendJson editSpendForUser(@Nonnull SpendJson spend) {
        Optional<SpendEntity> spendById = spendRepository.findById(spend.id());
        if (spendById.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can`t find spend by given id: " + spend.id());
        } else {
            final String category = spend.category();
            CategoryEntity categoryEntity = categoryRepository.findAllByUsername(spend.username())
                    .stream()
                    .filter(c -> c.getCategory().equals(category))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Can`t find category by given name: " + category));

            SpendEntity spendEntity = spendById.get();
            spendEntity.setSpendDate(spend.spendDate());
            spendEntity.setCategory(categoryEntity);
            spendEntity.setAmount(spend.amount());
            spendEntity.setDescription(spend.description());
            return SpendJson.fromEntity(spendRepository.save(spendEntity));
        }
    }

    @Transactional(readOnly = true)
    public @Nonnull
    List<SpendJson> getSpendsForUser(@Nonnull String username,
                                     @Nullable CurrencyValues filterCurrency,
                                     @Nullable Date dateFrom,
                                     @Nullable Date dateTo) {
        return getSpendsEntityForUser(username, filterCurrency, dateFrom, dateTo)
                .map(SpendJson::fromEntity)
                .toList();
    }

    @Transactional
    public void deleteSpends(@Nonnull String username, @Nonnull List<String> ids) {
        spendRepository.deleteByUsernameAndIdIn(username, ids.stream().map(UUID::fromString).toList());
    }

    @Transactional(readOnly = true)
    public @Nonnull
    List<StatisticJson> getStatistic(@Nonnull String username,
                                     @Nonnull CurrencyValues userCurrency,
                                     @Nullable CurrencyValues filterCurrency,
                                     @Nullable Date dateFrom,
                                     @Nullable Date dateTo) {
        List<SpendEntity> spendEntities = getSpendsEntityForUser(username, filterCurrency, dateFrom, dateTo).toList();
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

        categoryRepository.findAllByUsername(username).stream()
                .filter(c -> !spendsByCategory.containsKey(c.getCategory()))
                .map(c -> new StatisticByCategoryJson(
                        c.getCategory(),
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
        return se -> {
            if (statistic.dateFrom() == null) {
                return new StatisticJson(
                        se.getSpendDate(),
                        statistic.dateTo(),
                        statistic.currency(),
                        statistic.total(),
                        statistic.userDefaultCurrency(),
                        statistic.totalInUserDefaultCurrency(),
                        statistic.categoryStatistics()
                );
            } else {
                return statistic;
            }
        };
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
                        ? BigDecimal.valueOf(statistic.totalInUserDefaultCurrency()).add(
                        grpcCurrencyClient.calculate(se.getAmount(), se.getCurrency(), userCurrency)
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
        StatisticJson enrichedStatistic = statistic;
        for (SpendEntity spend : sortedSpends) {
            enrichedStatistic =
                    enrichStatisticTotalInUserCurrencyByAllStreamElements(
                            enrichStatisticTotalAmountByAllStreamElements(
                                    enrichStatisticDateFromByFirstStreamElement(
                                            enrichedStatistic
                                    ).apply(spend)
                            ).apply(spend), statisticCurrency, userCurrency)
                            .apply(spend);
        }
        return enrichedStatistic;
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

    private @Nonnull
    Stream<SpendEntity> getSpendsEntityForUser(@Nonnull String username,
                                               @Nullable CurrencyValues filterCurrency,
                                               @Nullable Date dateFrom,
                                               @Nullable Date dateTo) {
        dateTo = dateTo == null
                ? new Date()
                : dateTo;

        List<SpendEntity> spends = dateFrom == null
                ? spendRepository.findAllByUsernameAndSpendDateLessThanEqual(username, dateTo)
                : spendRepository.findAllByUsernameAndSpendDateGreaterThanEqualAndSpendDateLessThanEqual(username, dateFrom, dateTo);

        return spends.stream()
                .filter(se -> filterCurrency == null || se.getCurrency() == filterCurrency);
    }
}
