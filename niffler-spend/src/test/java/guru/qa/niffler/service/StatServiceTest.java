package guru.qa.niffler.service;

import guru.qa.niffler.data.CategoryEntity;
import guru.qa.niffler.data.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.StatisticJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class StatServiceTest {

    private StatService statService;

    private final CurrencyValues userCurrency = CurrencyValues.USD;

    private SpendEntity firstSpend, secondSpend, thirdSpend;
    private CategoryEntity firstCategory, secondCategory, thirdCategory;

    @BeforeEach
    void setUp(@Mock SpendService spendService,
               @Mock CategoryService categoryService,
               @Mock GrpcCurrencyClient grpcCurrencyClient) {
        firstCategory = new CategoryEntity();
        firstCategory.setCategory("Бар");
        firstCategory.setUsername("dima");
        secondCategory = new CategoryEntity();
        secondCategory.setCategory("Магазин");
        secondCategory.setUsername("dima");
        thirdCategory = new CategoryEntity();
        thirdCategory.setCategory("Рыбалка");
        thirdCategory.setUsername("dima");

        firstSpend = new SpendEntity();
        firstSpend.setCategory(firstCategory);
        firstSpend.setAmount(750.0);
        firstSpend.setDescription("Коктейль двойной дайкири");
        firstSpend.setCurrency(CurrencyValues.RUB);
        firstSpend.setSpendDate(addDaysToDate(new Date(), Calendar.DAY_OF_WEEK, -3));

        secondSpend = new SpendEntity();
        secondSpend.setCategory(firstCategory);
        secondSpend.setAmount(600.0);
        secondSpend.setDescription("Бокал пива");
        secondSpend.setCurrency(CurrencyValues.RUB);
        secondSpend.setSpendDate(addDaysToDate(new Date(), Calendar.DAY_OF_WEEK, -1));

        thirdSpend = new SpendEntity();
        thirdSpend.setCategory(thirdCategory);
        thirdSpend.setAmount(12000.0);
        thirdSpend.setDescription("Спининг");
        thirdSpend.setCurrency(CurrencyValues.RUB);
        thirdSpend.setSpendDate(addDaysToDate(new Date(), Calendar.DAY_OF_WEEK, -2));

        lenient().when(spendService.getSpendsEntityForUser(eq("dima"), any(CurrencyValues.class), isNull(), any(Date.class)))
                .thenReturn(List.of(
                        firstSpend, secondSpend, thirdSpend
                ));

        lenient().when(spendService.getSpendsEntityForUser(eq("dima"), any(CurrencyValues.class), any(Date.class), any(Date.class)))
                .thenReturn(List.of(
                        secondSpend, thirdSpend
                ));

        lenient().when(categoryService.getAllCategories(eq("dima")))
                .thenReturn(Stream.of(
                        firstCategory, secondCategory, thirdCategory
                ).map(CategoryJson::fromEntity).toList());

        lenient().when(grpcCurrencyClient.calculate(any(Double.class), eq(CurrencyValues.RUB), eq(CurrencyValues.USD)))
                .thenAnswer(a -> BigDecimal.valueOf((double) a.getArguments()[0] / 75.0));

        statService = new StatService(spendService, categoryService, grpcCurrencyClient);
    }

    @Test
    void getStatisticTest() {
        List<StatisticJson> result = statService.getStatistic("dima", userCurrency, null, null, null);
        assertEquals(4, result.size());
    }

    static Stream<Arguments> resolveDesiredCurrenciesInStatisticTest() {
        return Stream.of(
                Arguments.of(null, CurrencyValues.values()),
                Arguments.of(CurrencyValues.KZT, new CurrencyValues[]{CurrencyValues.KZT}),
                Arguments.of(CurrencyValues.USD, new CurrencyValues[]{CurrencyValues.USD})
        );
    }

    @MethodSource
    @ParameterizedTest
    void resolveDesiredCurrenciesInStatisticTest(CurrencyValues tested, CurrencyValues[] expected) {
        CurrencyValues[] currencyValues = statService.resolveDesiredCurrenciesInStatistic(tested);
        assertArrayEquals(expected, currencyValues);
    }

    @Test
    void createDefaultStatisticJsonTest() {
        Date dateTo = new Date();
        StatisticJson defaultStatisticJson = statService.createDefaultStatisticJson(CurrencyValues.KZT, userCurrency, dateTo);
        assertEquals(dateTo, defaultStatisticJson.dateTo());
        assertEquals(CurrencyValues.KZT, defaultStatisticJson.currency());
        assertEquals(userCurrency, defaultStatisticJson.userDefaultCurrency());
        assertEquals(0.0, defaultStatisticJson.total());
        assertEquals(0.0, defaultStatisticJson.totalInUserDefaultCurrency());
        assertNull(defaultStatisticJson.dateFrom());
        assertEquals(0, defaultStatisticJson.categoryStatistics().size());
    }

    @Test
    void enrichStatisticDateFromByFirstStreamElementTest() {
        Date dateTo = new Date();
        StatisticJson defaultStatisticJson = statService.createDefaultStatisticJson(CurrencyValues.KZT, userCurrency, dateTo);

        for (SpendEntity spend : Stream.of(secondSpend, firstSpend, thirdSpend)
                .sorted(Comparator.comparing(SpendEntity::getSpendDate))
                .toList()) {
            defaultStatisticJson = statService.enrichStatisticDateFromByFirstStreamElement(
                    defaultStatisticJson
            ).apply(spend);
        }

        assertEquals(firstSpend.getSpendDate(), defaultStatisticJson.dateFrom());
    }

    @Test
    void enrichStatisticTotalAmountByAllStreamElementsTest() {
        Date dateTo = new Date();
        StatisticJson defaultStatisticJson = statService.createDefaultStatisticJson(CurrencyValues.KZT, userCurrency, dateTo);

        for (SpendEntity spend : Stream.of(secondSpend, firstSpend, thirdSpend)
                .sorted(Comparator.comparing(SpendEntity::getSpendDate))
                .toList()) {
            defaultStatisticJson = statService.enrichStatisticTotalAmountByAllStreamElements(
                    defaultStatisticJson
            ).apply(spend);
        }

        assertEquals(13350.0, defaultStatisticJson.total());
    }

    @Test
    void enrichStatisticTotalInUserCurrencyByAllStreamElementsSameCurrencyTest() {
        Date dateTo = new Date();
        CurrencyValues statisticCurrency = CurrencyValues.RUB;
        CurrencyValues userCurrency = CurrencyValues.RUB;
        StatisticJson defaultStatisticJson = statService.createDefaultStatisticJson(statisticCurrency, userCurrency, dateTo);

        for (SpendEntity spend : Stream.of(secondSpend, firstSpend, thirdSpend)
                .sorted(Comparator.comparing(SpendEntity::getSpendDate))
                .toList()) {
            defaultStatisticJson =
                    statService.enrichStatisticTotalInUserCurrencyByAllStreamElements(
                                    statService.enrichStatisticTotalAmountByAllStreamElements(defaultStatisticJson)
                                            .apply(spend), statisticCurrency, userCurrency)
                            .apply(spend);
        }

        assertEquals(13350.0, defaultStatisticJson.total());
        assertEquals(13350.0, defaultStatisticJson.totalInUserDefaultCurrency());
    }

    @Test
    void enrichStatisticTotalInUserCurrencyByAllStreamElementsDifferentCurrencyTest() {
        Date dateTo = new Date();
        CurrencyValues statisticCurrency = CurrencyValues.RUB;
        CurrencyValues userCurrency = CurrencyValues.USD;
        StatisticJson defaultStatisticJson = statService.createDefaultStatisticJson(statisticCurrency, userCurrency, dateTo);

        for (SpendEntity spend : Stream.of(secondSpend, firstSpend, thirdSpend)
                .sorted(Comparator.comparing(SpendEntity::getSpendDate))
                .toList()) {
            defaultStatisticJson =
                    statService.enrichStatisticTotalInUserCurrencyByAllStreamElements(
                                    statService.enrichStatisticTotalAmountByAllStreamElements(defaultStatisticJson)
                                            .apply(spend), statisticCurrency, userCurrency)
                            .apply(spend);
        }
        assertEquals(13350.0, defaultStatisticJson.total());
        assertEquals(178.0, defaultStatisticJson.totalInUserDefaultCurrency());
    }

    @Test
    void bindSpendsToCategoriesTest() {
        Date dateTo = new Date();
        CurrencyValues statisticCurrency = CurrencyValues.RUB;
        StatisticJson defaultStatisticJson = statService.createDefaultStatisticJson(statisticCurrency, userCurrency, dateTo);
        List<SpendEntity> sortedSpends = List.of(secondSpend, firstSpend, thirdSpend).stream()
                .filter(se -> se.getCurrency() == statisticCurrency)
                .sorted(Comparator.comparing(SpendEntity::getSpendDate))
                .toList();

        Map<String, List<SpendJson>> map = statService.bindSpendsToCategories(sortedSpends);

        assertEquals(2, map.size());
        assertNotNull(map.get("Бар"));
        assertNotNull(map.get("Рыбалка"));
        List<SpendJson> barSpends = map.get("Бар");
        List<SpendJson> fishCatchSpends = map.get("Рыбалка");
        assertEquals(2, barSpends.size());
        assertEquals(1, fishCatchSpends.size());
    }

    private Date addDaysToDate(Date date, int selector, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(selector, days);
        return cal.getTime();
    }
}