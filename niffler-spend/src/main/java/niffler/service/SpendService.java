package niffler.service;

import jakarta.annotation.Nullable;
import niffler.data.CategoryEntity;
import niffler.data.SpendEntity;
import niffler.data.repository.CategoryRepository;
import niffler.data.repository.SpendRepository;
import niffler.model.CurrencyValues;
import niffler.model.DataFilterValues;
import niffler.model.SpendJson;
import niffler.model.StatisticByCategoryJson;
import niffler.model.StatisticJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SpendService {

    private final SpendRepository spendRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public SpendService(SpendRepository spendRepository, CategoryRepository categoryRepository) {
        this.spendRepository = spendRepository;
        this.categoryRepository = categoryRepository;
    }

    public SpendJson saveSpendForUser(SpendJson spend) {
        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setUsername(spend.getUsername());
        spendEntity.setSpendDate(spend.getSpendDate());
        spendEntity.setCurrency(spend.getCurrency());
        spendEntity.setDescription(spend.getDescription());
        spendEntity.setAmount(spend.getAmount());

        CategoryEntity categoryEntity = categoryRepository.findByDescription(spend.getCategory());
        if (categoryEntity == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Can`t find category by given name: " + spend.getCategory());
        }
        spendEntity.setCategory(categoryEntity);

        return SpendJson.fromEntity(spendRepository.save(spendEntity));
    }

    public List<SpendJson> getSpendsForUser(String username, @Nullable DataFilterValues filter, @Nullable CurrencyValues currency) {
        Date filterDate = filterDate(filter);
        List<SpendEntity> spends = filterDate == null
                ? spendRepository.findAllByUsername(username)
                : spendRepository.findAllByUsernameAndSpendDateGreaterThanEqual(username, filterDate);

        return spends.stream()
                .filter(se -> currency == null || se.getCurrency() == currency)
                .map(SpendJson::fromEntity)
                .collect(Collectors.toList());
    }

    public List<StatisticJson> getStatistic(String username,
                                            @Nullable CurrencyValues currency,
                                            @Nullable Date dateFrom,
                                            @Nullable Date dateTo) {
        if (dateTo == null) {
            dateTo = new Date();
        }
        List<SpendEntity> spendEntities;
        List<StatisticJson> result = new ArrayList<>();

        if (dateFrom == null) {
            spendEntities = spendRepository.findAllByUsernameAndSpendDateLessThanEqual(
                    username,
                    dateTo
            );
        } else {
            spendEntities = spendRepository.findAllByUsernameAndSpendDateGreaterThanEqualAndSpendDateLessThanEqual(
                    username,
                    dateFrom,
                    dateTo
            );
        }

        CurrencyValues[] desiredCurrenciesInResponse = currency != null ? new CurrencyValues[]{currency} : CurrencyValues.values();

        for (CurrencyValues value : desiredCurrenciesInResponse) {
            StatisticJson statistic = new StatisticJson();
            statistic.setDateTo(dateTo);
            statistic.setCurrency(value);
            statistic.setTotal(0.0);

            Map<String, List<SpendJson>> spendsByCategory = spendEntities.stream()
                    .filter(se -> se.getCurrency() == value)
                    .sorted(Comparator.comparing(SpendEntity::getSpendDate))
                    .peek(se -> {
                        if (statistic.getDateFrom() == null) {
                            statistic.setDateFrom(se.getSpendDate());
                        }
                    })
                    .peek(se -> statistic.setTotal(BigDecimal.valueOf(statistic.getTotal())
                            .add(BigDecimal.valueOf(se.getAmount()))
                            .doubleValue())
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
                sbcjResult.add(sbcj);
            }

            categoryRepository.findAll().stream()
                    .filter(c -> !spendsByCategory.containsKey(c.getDescription()))
                    .map(c -> {
                        StatisticByCategoryJson sbcj = new StatisticByCategoryJson();
                        sbcj.setCategory(c.getDescription());
                        sbcj.setSpends(Collections.EMPTY_LIST);
                        sbcj.setTotal(0.0);
                        return sbcj;
                    })
                    .forEach(sbcjResult::add);

            statistic.setCategoryStatistics(sbcjResult);
            result.add(statistic);
        }
        return result;
    }

    private @Nullable
    Date filterDate(@Nullable DataFilterValues filter) {
        Date currentDate = new Date();
        if (filter != null) {
            return switch (filter) {
                case TODAY -> currentDate;
                case YESTERDAY -> addDaysToDate(currentDate, Calendar.DAY_OF_WEEK, -1);
                case WEEK -> addDaysToDate(currentDate, Calendar.WEEK_OF_MONTH, -1);
                case MONTH -> addDaysToDate(currentDate, Calendar.MONTH, -1);
                case ALL -> null;
            };
        }
        return null;
    }

    private Date addDaysToDate(Date date, int selector, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(selector, days);
        return cal.getTime();
    }
}
