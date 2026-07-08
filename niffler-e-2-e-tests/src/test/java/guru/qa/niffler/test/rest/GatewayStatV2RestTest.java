package guru.qa.niffler.test.rest;

import guru.qa.niffler.api.GatewayV2ApiClient;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.DataFilterValues;
import guru.qa.niffler.model.rest.StatisticV2Json;
import guru.qa.niffler.model.rest.SumByCategory;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static guru.qa.niffler.model.rest.CurrencyValues.RUB;
import static guru.qa.niffler.model.rest.CurrencyValues.USD;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import javax.annotation.ParametersAreNonnullByDefault;

@Epic("[REST][niffler-gateway]: Stat V2")
@DisplayName("[REST][niffler-gateway]: Stat V2")
@ParametersAreNonnullByDefault
public class GatewayStatV2RestTest extends BaseRestTest {

  private static final GatewayV2ApiClient gatewayV2client = new GatewayV2ApiClient();

  @Test
  @AllureId("200044")
  @DisplayName("REST: GET /api/v2/stat/total возвращает StatisticV2Json с суммой по категориям")
  @Tag("REST")
  @ApiLogin(user = @GenerateUser(
      categories = {@GenerateCategory(name = "Бар"), @GenerateCategory(name = "Еда")},
      spends = {
          @GenerateSpend(name = "Коктейль", category = "Бар", amount = 650, currency = RUB),
          @GenerateSpend(name = "Бургер", category = "Еда", amount = 350, currency = RUB),
      }
  ))
  void statV2BasicTest(@Token String bearerToken) throws Exception {
    final StatisticV2Json stat = gatewayV2client.totalStat(bearerToken, null, null, null);

    step("Check that stat is not null", () ->
        assertNotNull(stat)
    );
    step("Check that total equals sum of all spends", () ->
        assertEquals(1000.0, stat.total())
    );
    step("Check that currency is RUB (user default)", () ->
        assertEquals(CurrencyValues.RUB, stat.currency())
    );
    step("Check that statByCategories contains 2 categories", () ->
        assertEquals(2, stat.statByCategories().size())
    );
  }

  @Test
  @AllureId("200045")
  @DisplayName("REST: GET /api/v2/stat/total?filterPeriod=WEEK возвращает только траты за неделю")
  @Tag("REST")
  @ApiLogin(user = @GenerateUser(
      categories = @GenerateCategory(name = "Бар"),
      spends = {
          @GenerateSpend(name = "Сегодня", category = "Бар", amount = 300, currency = RUB, addDaysToSpendDate = 0),
          @GenerateSpend(name = "Старая", category = "Бар", amount = 1000, currency = RUB, addDaysToSpendDate = -10),
      }
  ))
  void statV2WithFilterPeriodTest(@Token String bearerToken) throws Exception {
    final StatisticV2Json stat = gatewayV2client.totalStat(
        bearerToken, null, null, DataFilterValues.WEEK
    );

    step("Check that only current week spend is counted", () ->
        assertEquals(300.0, stat.total())
    );
  }

  @Test
  @AllureId("200046")
  @DisplayName("REST: GET /api/v2/stat/total?statCurrency=USD&filterCurrency=USD возвращает только траты в USD")
  @Tag("REST")
  @ApiLogin(user = @GenerateUser(
      categories = @GenerateCategory(name = "Бар"),
      spends = {
          @GenerateSpend(name = "RUB spend", category = "Бар", amount = 650, currency = RUB),
          @GenerateSpend(name = "USD spend", category = "Бар", amount = 5, currency = CurrencyValues.USD),
      }
  ))
  void statV2WithFilterCurrencyTest(@Token String bearerToken) throws Exception {
    final StatisticV2Json stat = gatewayV2client.totalStat(
        bearerToken, USD, USD, null
    );

    step("Check that stat is not null", () ->
        assertNotNull(stat)
    );
    final List<SumByCategory> categories = stat.statByCategories();
    step("Check that statistic currency is USD", () ->
        assertEquals(USD, stat.currency())
    );
    step("Check that only USD spend is in stat", () -> {
      assertEquals(5.0, stat.total());
      assertEquals(1, categories.size());
      assertEquals(5.0, categories.getFirst().sum());
      assertEquals(USD, categories.getFirst().currency());
    });
  }
}
