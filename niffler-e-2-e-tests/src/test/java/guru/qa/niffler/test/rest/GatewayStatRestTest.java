package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.DataFilterValues;
import guru.qa.niffler.model.rest.StatisticByCategoryJson;
import guru.qa.niffler.model.rest.StatisticJson;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static guru.qa.niffler.model.rest.CurrencyValues.EUR;
import static guru.qa.niffler.model.rest.CurrencyValues.KZT;
import static guru.qa.niffler.model.rest.CurrencyValues.RUB;
import static guru.qa.niffler.model.rest.CurrencyValues.USD;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Epic("[REST][niffler-gateway]: Stat")
@DisplayName("[REST][niffler-gateway]: Stat")
public class GatewayStatRestTest extends BaseRestTest {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    @DisplayName("REST: Статистика содержит корректные данные при рассчете за все время и без фильтрации по валюте")
    @AllureId("200009")
    @Tag("REST")
    @ApiLogin(
            user = @GenerateUser(
                    categories = {@GenerateCategory("Бар"), @GenerateCategory("Азс")},
                    spends = {
                            @GenerateSpend(spendName = "Коктейль", spendCategory = "Бар", amount = 650, currency = RUB, addDaysToSpendDate = -2),
                            @GenerateSpend(spendName = "Кофе", spendCategory = "Бар", amount = 200, currency = RUB, addDaysToSpendDate = -1),
                            @GenerateSpend(spendName = "Орешки", spendCategory = "Бар", amount = 300, currency = RUB),
                    }
            )
    )
    void statForOpenPeriodWithoutFilteringTest(@User UserJson user,
                                               @Token String bearerToken) throws Exception {
        final List<StatisticJson> statistic = gatewayApiClient.totalStat(
                bearerToken,
                null,
                null
        );

        step("Check that response contains statistic for each system currencies", () ->
                assertEquals(CurrencyValues.values().length, statistic.size())
        );

        final StatisticJson rubStat = statistic.stream().filter(s -> s.currency() == RUB).findFirst().orElseThrow();
        final StatisticJson usdStat = statistic.stream().filter(s -> s.currency() == USD).findFirst().orElseThrow();
        final StatisticJson eurStat = statistic.stream().filter(s -> s.currency() == EUR).findFirst().orElseThrow();
        final StatisticJson kztStat = statistic.stream().filter(s -> s.currency() == KZT).findFirst().orElseThrow();

        final Date expectedFromDateInStat = user.testData().spends().get(0).spendDate();
        // Check RUB stat
        step("Check that `dateFrom` date equal to the earliest date of spends", () ->
                assertEquals(formatter.format(expectedFromDateInStat), formatter.format(rubStat.dateFrom()))
        );
        step("Check that `dateTo` date is null", () ->
                assertNull(rubStat.dateTo())
        );
        step("Check that `total` is equal to sum of all spends", () ->
                assertEquals(1150.0, rubStat.total())
        );
        step("Check that `totalInUserDefaultCurrency` (RUB) is equal to sum of all spends", () ->
                assertEquals(1150.0, rubStat.totalInUserDefaultCurrency())
        );
        step("Check statistic by categories has size equal to user categories count", () ->
                assertEquals(user.testData().categories().size(), rubStat.categoryStatistics().size())
        );

        final StatisticByCategoryJson rubBarStat = rubStat.categoryStatistics().stream()
                .filter(cs -> "Бар".equals(cs.category())).findFirst().orElseThrow();
        final StatisticByCategoryJson rubFuelStat = rubStat.categoryStatistics().stream()
                .filter(cs -> "Азс".equals(cs.category())).findFirst().orElseThrow();

        step("Check that `total` is equal to sum of all spends by category `Бар`", () ->
                assertEquals(1150.0, rubBarStat.total())
        );
        step("Check that `totalInUserDefaultCurrency` (RUB) is equal to sum of all spends by category `Бар`", () ->
                assertEquals(1150.0, rubBarStat.totalInUserDefaultCurrency())
        );
        step("Check spends in category `Бар` has size equal to user spends for category", () ->
                assertEquals(3, rubBarStat.spends().size())
        );
        step("Check that `total` is equal to sum of all spends by category `Азс`", () ->
                assertEquals(0.0, rubFuelStat.total())
        );
        step("Check that `totalInUserDefaultCurrency` (RUB) is equal to sum of all spends by category `Азс`", () ->
                assertEquals(0.0, rubFuelStat.totalInUserDefaultCurrency())
        );
        step("Check spends in category `Азс` has size equal to user spends for category", () ->
                assertEquals(0, rubFuelStat.spends().size())
        );

        // Check USD, EUR, KZT stat
        step("Check that `dateFrom` date is null", () ->
                assertAll(() -> {
                    assertNull(usdStat.dateFrom());
                    assertNull(eurStat.dateFrom());
                    assertNull(kztStat.dateFrom());
                })
        );
        step("Check that `dateTo` date is null", () ->
                assertAll(() -> {
                    assertNull(usdStat.dateTo());
                    assertNull(eurStat.dateTo());
                    assertNull(kztStat.dateTo());
                })
        );
        step("Check that `total` is equal to 0.0", () ->
                assertAll(() -> {
                    assertEquals(0.0, usdStat.total());
                    assertEquals(0.0, eurStat.total());
                    assertEquals(0.0, kztStat.total());
                })
        );
        step("Check that `totalInUserDefaultCurrency` (RUB) is equal to 0.0", () ->
                assertAll(() -> {
                    assertEquals(0.0, usdStat.totalInUserDefaultCurrency());
                    assertEquals(0.0, eurStat.totalInUserDefaultCurrency());
                    assertEquals(0.0, kztStat.totalInUserDefaultCurrency());
                })
        );
        step("Check statistic by categories has size equal to user categories count", () ->
                assertAll(() -> {
                    assertEquals(user.testData().categories().size(), usdStat.categoryStatistics().size());
                    assertEquals(user.testData().categories().size(), eurStat.categoryStatistics().size());
                    assertEquals(user.testData().categories().size(), kztStat.categoryStatistics().size());
                })
        );

        final StatisticByCategoryJson usdBarStat = usdStat.categoryStatistics().stream()
                .filter(cs -> "Бар".equals(cs.category())).findFirst().orElseThrow();
        final StatisticByCategoryJson usdFuelStat = usdStat.categoryStatistics().stream()
                .filter(cs -> "Азс".equals(cs.category())).findFirst().orElseThrow();
        final StatisticByCategoryJson eurBarStat = eurStat.categoryStatistics().stream()
                .filter(cs -> "Бар".equals(cs.category())).findFirst().orElseThrow();
        final StatisticByCategoryJson eurFuelStat = eurStat.categoryStatistics().stream()
                .filter(cs -> "Азс".equals(cs.category())).findFirst().orElseThrow();
        final StatisticByCategoryJson kztBarStat = kztStat.categoryStatistics().stream()
                .filter(cs -> "Бар".equals(cs.category())).findFirst().orElseThrow();
        final StatisticByCategoryJson kztFuelStat = kztStat.categoryStatistics().stream()
                .filter(cs -> "Азс".equals(cs.category())).findFirst().orElseThrow();

        step("Check that `total` is equal to sum of all spends by category `Бар`", () ->
                assertAll(() -> {
                    assertEquals(0.0, usdBarStat.total());
                    assertEquals(0.0, eurBarStat.total());
                    assertEquals(0.0, kztBarStat.total());
                })
        );
        step("Check that `totalInUserDefaultCurrency` (RUB) is equal to sum of all spends by category `Бар`", () ->
                assertAll(() -> {
                    assertEquals(0.0, usdBarStat.totalInUserDefaultCurrency());
                    assertEquals(0.0, eurBarStat.totalInUserDefaultCurrency());
                    assertEquals(0.0, kztBarStat.totalInUserDefaultCurrency());
                })
        );
        step("Check spends in category `Бар` has size equal to user spends for category", () ->
                assertAll(() -> {
                    assertEquals(0, usdBarStat.spends().size());
                    assertEquals(0, eurBarStat.spends().size());
                    assertEquals(0, kztBarStat.spends().size());
                })
        );

        step("Check that `total` is equal to sum of all spends by category `Азс`", () ->
                assertAll(() -> {
                    assertEquals(0.0, usdFuelStat.total());
                    assertEquals(0.0, eurFuelStat.total());
                    assertEquals(0.0, kztFuelStat.total());
                })
        );
        step("Check that `totalInUserDefaultCurrency` (RUB) is equal to sum of all spends by category `Азс`", () ->
                assertAll(() -> {
                    assertEquals(0.0, usdFuelStat.totalInUserDefaultCurrency());
                    assertEquals(0.0, eurFuelStat.totalInUserDefaultCurrency());
                    assertEquals(0.0, kztFuelStat.totalInUserDefaultCurrency());
                })
        );
        step("Check spends in category `Азс` has size equal to user spends for category", () ->
                assertAll(() -> {
                    assertEquals(0, usdFuelStat.spends().size());
                    assertEquals(0, eurFuelStat.spends().size());
                    assertEquals(0, kztFuelStat.spends().size());
                })
        );
    }

    @Test
    @DisplayName("REST: Статистика содержит корректные данные при рассчете за все время c фильтрацией по валюте")
    @AllureId("200010")
    @Tag("REST")
    @ApiLogin(
            user = @GenerateUser(
                    categories = @GenerateCategory("Бар"),
                    spends = {
                            @GenerateSpend(spendName = "Коктейль", spendCategory = "Бар", amount = 650, currency = RUB),
                            @GenerateSpend(spendName = "Кофе", spendCategory = "Бар", amount = 200, currency = RUB),
                            @GenerateSpend(spendName = "Орешки", spendCategory = "Бар", amount = 4, currency = USD, addDaysToSpendDate = -2),
                    }
            )
    )
    void statForOpenPeriodWithFilteringTest(@User UserJson user,
                                            @Token String bearerToken) throws Exception {
        final List<StatisticJson> statistic = gatewayApiClient.totalStat(
                bearerToken,
                USD,
                null
        );
        step("Check that response contains statistic only for USD (filterCurrency)", () ->
                assertEquals(1, statistic.size())
        );

        final StatisticJson usdStat = statistic.stream().filter(s -> s.currency() == USD).findFirst().orElseThrow();

        final Date expectedFromDateInStat = user.testData().spends().get(2).spendDate();
        // Check USD stat
        step("Check that `dateFrom` date equal to the earliest date of spends", () ->
                assertEquals(formatter.format(expectedFromDateInStat), formatter.format(usdStat.dateFrom()))
        );
        step("Check that `dateTo` date is null", () ->
                assertNull(usdStat.dateTo())
        );
        step("Check that `total` is equal to sum of all spends", () ->
                assertEquals(4.0, usdStat.total())
        );
        step("Check that `totalInUserDefaultCurrency` (RUB) is equal to sum of all spends", () ->
                assertEquals(266.67, usdStat.totalInUserDefaultCurrency())
        );
        step("Check statistic by categories has size equal to user categories count", () ->
                assertEquals(user.testData().categories().size(), usdStat.categoryStatistics().size())
        );

        final StatisticByCategoryJson usdBarStat = usdStat.categoryStatistics().get(0);

        step("Check that `total` is equal to sum of all spends by category `Бар`", () ->
                assertEquals(4.0, usdBarStat.total())
        );
        step("Check that `totalInUserDefaultCurrency` (RUB) is equal to sum of all spends by category `Бар`", () ->
                assertEquals(266.67, usdBarStat.totalInUserDefaultCurrency())
        );
        step("Check spends in category `Бар` has size equal to user spends for category", () ->
                assertEquals(1, usdBarStat.spends().size())
        );
    }

    @Test
    @DisplayName("REST: Статистика содержит корректные данные при рассчете за период c фильтрацией по валюте")
    @AllureId("200011")
    @Tag("REST")
    @ApiLogin(
            user = @GenerateUser(
                    categories = {@GenerateCategory("Бар"), @GenerateCategory("Азс")},
                    spends = {
                            @GenerateSpend(spendName = "Коктейль", spendCategory = "Бар", amount = 650, currency = RUB, addDaysToSpendDate = -2),
                            @GenerateSpend(spendName = "Бензин", spendCategory = "Азс", amount = 2000, currency = RUB, addDaysToSpendDate = -3),
                            @GenerateSpend(spendName = "Орешки", spendCategory = "Бар", amount = 300, currency = RUB, addDaysToSpendDate = -8),
                    }
            )
    )
    void statForPeriodWithFilteringTest(@User UserJson user,
                                        @Token String bearerToken) throws Exception {
        final String dateTo = formatter.format(new Date());
        final List<StatisticJson> statistic = gatewayApiClient.totalStat(
                bearerToken,
                null,
                DataFilterValues.WEEK
        );

        step("Check that response contains statistic for each system currencies", () ->
                assertEquals(CurrencyValues.values().length, statistic.size())
        );

        final StatisticJson rubStat = statistic.stream().filter(s -> s.currency() == RUB).findFirst().orElseThrow();

        final Date expectedFromDateInStat = user.testData().spends().get(1).spendDate();
        // Check RUB stat
        step("Check that `dateFrom` date equal to the earliest date of spends", () ->
                assertEquals(formatter.format(expectedFromDateInStat), formatter.format(rubStat.dateFrom()))
        );
        step("Check that `dateTo` date equal to request parameter", () ->
                assertEquals(dateTo, formatter.format(rubStat.dateTo()))
        );
        step("Check that `total` is equal to sum of all spends", () ->
                assertEquals(2650.0, rubStat.total())
        );
        step("Check that `totalInUserDefaultCurrency` (RUB) is equal to sum of all spends", () ->
                assertEquals(2650.0, rubStat.totalInUserDefaultCurrency())
        );
        step("Check statistic by categories has size equal to user categories count", () ->
                assertEquals(user.testData().categories().size(), rubStat.categoryStatistics().size())
        );

        final StatisticByCategoryJson rubBarStat = rubStat.categoryStatistics().stream()
                .filter(cs -> "Бар".equals(cs.category())).findFirst().orElseThrow();
        final StatisticByCategoryJson rubFuelStat = rubStat.categoryStatistics().stream()
                .filter(cs -> "Азс".equals(cs.category())).findFirst().orElseThrow();

        step("Check that `total` is equal to sum of all spends by category `Бар`", () ->
                assertEquals(650.0, rubBarStat.total())
        );
        step("Check that `totalInUserDefaultCurrency` (RUB) is equal to sum of all spends by category `Бар`", () ->
                assertEquals(650.0, rubBarStat.totalInUserDefaultCurrency())
        );
        step("Check spends in category `Бар` has size equal to user spends for category", () ->
                assertEquals(1, rubBarStat.spends().size())
        );
        step("Check that `total` is equal to sum of all spends by category `Азс`", () ->
                assertEquals(2000.0, rubFuelStat.total())
        );
        step("Check that `totalInUserDefaultCurrency` (RUB) is equal to sum of all spends by category `Азс`", () ->
                assertEquals(2000.0, rubFuelStat.totalInUserDefaultCurrency())
        );
        step("Check spends in category `Азс` has size equal to user spends for category", () ->
                assertEquals(1, rubFuelStat.spends().size())
        );
    }
}
