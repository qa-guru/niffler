package niffler.service;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import niffler.data.CategoryEntity;
import niffler.data.SpendEntity;
import niffler.data.repository.CategoryRepository;
import niffler.data.repository.SpendRepository;
import niffler.model.CurrencyValues;
import niffler.model.SpendJson;
import niffler.model.StatisticByCategoryJson;
import niffler.model.StatisticJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
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
import java.util.function.Consumer;
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

    public @Nonnull
    SpendJson saveSpendForUser(@Nonnull SpendJson spend) {
        final String username = spend.getUsername();
        final String category = spend.getCategory();

        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setUsername(username);
        spendEntity.setSpendDate(spend.getSpendDate());
        spendEntity.setCurrency(spend.getCurrency());
        spendEntity.setDescription(spend.getDescription());
        spendEntity.setAmount(spend.getAmount());

        CategoryEntity categoryEntity = categoryRepository.findAllByUsername(username)
                .stream()
                .filter(c -> c.getCategory().equals(category))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Can`t find category by given name: " + category));

        spendEntity.setCategory(categoryEntity);
        return SpendJson.fromEntity(spendRepository.save(spendEntity));
    }

    public @Nonnull
    SpendJson editSpendForUser(@Nonnull SpendJson spend) {
        Optional<SpendEntity> spendById = spendRepository.findById(spend.getId());
        if (spendById.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can`t find spend by given id: " + spend.getId());
        } else {
            final String category = spend.getCategory();
            CategoryEntity categoryEntity = categoryRepository.findAllByUsername(spend.getUsername())
                    .stream()
                    .filter(c -> c.getCategory().equals(category))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Can`t find category by given name: " + category));

            SpendEntity spendEntity = spendById.get();
            spendEntity.setSpendDate(spend.getSpendDate());
            spendEntity.setCategory(categoryEntity);
            spendEntity.setAmount(spend.getAmount());
            spendEntity.setDescription(spend.getDescription());
            return SpendJson.fromEntity(spendRepository.save(spendEntity));
        }
    }

    public @Nonnull
    List<SpendJson> getSpendsForUser(@Nonnull String username,
                                     @Nullable CurrencyValues filterCurrency,
                                     @Nullable Date dateFrom,
                                     @Nullable Date dateTo) {
        return getSpendsEntityForUser(username, filterCurrency, dateFrom, dateTo)
                .map(SpendJson::fromEntity)
                .collect(Collectors.toList());
    }

    public void deleteSpends(@Nonnull String username, @Nonnull List<String> ids) {
        for (String id : ids) {
            spendRepository.delete(getSpendsEntityForUser(username, null, null, null)
                    .filter(se -> se.getId().toString().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Spend not found by given id: " + id)));
        }
    }

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
            StatisticJson enriched = enrichStatistic(statisticCurrency, username, userCurrency, spendEntities, dateTo);
            result.add(enriched);
        }
        return result;
    }

    @Nonnull
    StatisticJson enrichStatistic(@Nonnull CurrencyValues statisticCurrency,
                                  @Nonnull String username,
                                  @Nonnull CurrencyValues userCurrency,
                                  @Nonnull List<SpendEntity> spendEntities,
                                  @Nullable Date dateTo) {
        StatisticJson statistic = createDefaultStatisticJson(statisticCurrency, userCurrency, dateTo);
        Map<String, List<SpendJson>> spendsByCategory = bindSpendsToCategories(statistic, statisticCurrency, userCurrency, spendEntities);

        List<StatisticByCategoryJson> sbcjResult = new ArrayList<>();
        for (Map.Entry<String, List<SpendJson>> entry : spendsByCategory.entrySet()) {
            StatisticByCategoryJson sbcj = new StatisticByCategoryJson();
            sbcj.setCategory(entry.getKey());
            sbcj.setSpends(entry.getValue());
            sbcj.setTotal(entry.getValue().stream()
                    .map(SpendJson::getAmount)
                    .map(BigDecimal::valueOf)
                    .reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue());
            sbcj.setTotalInUserDefaultCurrency(
                    grpcCurrencyClient.calculate(
                            sbcj.getTotal(),
                            statisticCurrency,
                            userCurrency
                    ).doubleValue()
            );
            sbcjResult.add(sbcj);
        }

        categoryRepository.findAllByUsername(username).stream()
                .filter(c -> !spendsByCategory.containsKey(c.getCategory()))
                .map(c -> {
                    StatisticByCategoryJson sbcj = new StatisticByCategoryJson();
                    sbcj.setCategory(c.getCategory());
                    sbcj.setSpends(Collections.emptyList());
                    sbcj.setTotal(0.0);
                    sbcj.setTotalInUserDefaultCurrency(0.0);
                    return sbcj;
                })
                .forEach(sbcjResult::add);

        sbcjResult.sort(Comparator.comparing(StatisticByCategoryJson::getCategory));
        statistic.setCategoryStatistics(sbcjResult);
        return statistic;
    }

    @Nonnull
    Consumer<SpendEntity> enrichStatisticDateFromByFirstStreamElement(@Nonnull StatisticJson statistic) {
        return se -> {
            if (statistic.getDateFrom() == null) {
                statistic.setDateFrom(se.getSpendDate());
            }
        };
    }

    @Nonnull
    Consumer<SpendEntity> enrichStatisticTotalAmountByAllStreamElements(@Nonnull StatisticJson statistic) {
        return se ->
                statistic.setTotal(BigDecimal.valueOf(statistic.getTotal())
                        .add(BigDecimal.valueOf(se.getAmount()))
                        .doubleValue());
    }

    @Nonnull
    Consumer<SpendEntity> enrichStatisticTotalInUserCurrencyByAllStreamElements(@Nonnull StatisticJson statistic,
                                                                                @Nonnull CurrencyValues statisticCurrency,
                                                                                @Nonnull CurrencyValues userCurrency) {
        return se -> {
            if (userCurrency != statisticCurrency) {
                statistic
                        .setTotalInUserDefaultCurrency(BigDecimal.valueOf(statistic.getTotalInUserDefaultCurrency())
                                .add(grpcCurrencyClient.calculate(
                                        se.getAmount(),
                                        se.getCurrency(),
                                        userCurrency
                                )).doubleValue());
            } else {
                statistic.setTotalInUserDefaultCurrency(statistic.getTotal());
            }
        };
    }

    @Nonnull
    Map<String, List<SpendJson>> bindSpendsToCategories(@Nonnull StatisticJson statistic,
                                                        @Nonnull CurrencyValues statisticCurrency,
                                                        @Nonnull CurrencyValues userCurrency,
                                                        @Nonnull List<SpendEntity> spendEntities) {
        return spendEntities.stream()
                .filter(se -> se.getCurrency() == statisticCurrency)
                .sorted(Comparator.comparing(SpendEntity::getSpendDate))
                .peek(enrichStatisticDateFromByFirstStreamElement(statistic))
                .peek(enrichStatisticTotalAmountByAllStreamElements(statistic))
                .peek(enrichStatisticTotalInUserCurrencyByAllStreamElements(statistic, statisticCurrency, userCurrency))
                .map(SpendJson::fromEntity)
                .collect(Collectors.groupingBy(
                        SpendJson::getCategory,
                        HashMap::new,
                        Collectors.toCollection(ArrayList::new)
                ));
    }

    @Nonnull
    StatisticJson createDefaultStatisticJson(@Nonnull CurrencyValues statisticCurrency,
                                             @Nonnull CurrencyValues userCurrency,
                                             @Nullable Date dateTo) {
        StatisticJson statistic = new StatisticJson();
        statistic.setDateTo(dateTo);
        statistic.setCurrency(statisticCurrency);
        statistic.setUserDefaultCurrency(userCurrency);
        statistic.setTotal(0.0);
        statistic.setTotalInUserDefaultCurrency(0.0);
        return statistic;
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
