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
        CurrencyValues[] desiredCurrenciesInResponse = filterCurrency != null
                ? new CurrencyValues[]{filterCurrency}
                : CurrencyValues.values();

        for (CurrencyValues value : desiredCurrenciesInResponse) {
            StatisticJson statistic = new StatisticJson();
            statistic.setDateTo(dateTo);
            statistic.setCurrency(value);
            statistic.setUserDefaultCurrency(userCurrency);
            statistic.setTotal(0.0);
            statistic.setTotalInUserDefaultCurrency(0.0);

            Map<String, List<SpendJson>> spendsByCategory = spendEntities.stream()
                    .filter(se -> se.getCurrency() == value)
                    .sorted(Comparator.comparing(SpendEntity::getSpendDate))
                    .peek(se -> {
                        if (statistic.getDateFrom() == null) {
                            statistic.setDateFrom(se.getSpendDate());
                        }
                    })
                    .peek(se ->
                            statistic.setTotal(BigDecimal.valueOf(statistic.getTotal())
                                    .add(BigDecimal.valueOf(se.getAmount()))
                                    .doubleValue())
                    )
                    .peek(se -> {
                                if (userCurrency != value) {
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
                            }
                    )
                    .map(SpendJson::fromEntity)
                    .collect(Collectors.groupingBy(
                            SpendJson::getCategory,
                            HashMap::new,
                            Collectors.toCollection(ArrayList::new)
                    ));

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
                                value,
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
            result.add(statistic);
        }
        return result;
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
