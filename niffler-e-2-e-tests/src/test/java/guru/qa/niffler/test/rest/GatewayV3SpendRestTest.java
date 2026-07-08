package guru.qa.niffler.test.rest;

import guru.qa.niffler.api.GatewayV3ApiClient;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.model.page.PagedModelJson;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.data.domain.Sort;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import javax.annotation.ParametersAreNonnullByDefault;

@Epic("[REST][niffler-gateway]: Пагинация Spends V3")
@DisplayName("[REST][niffler-gateway]: Пагинация Spends V3")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ParametersAreNonnullByDefault
public class GatewayV3SpendRestTest extends BaseRestTest {

  private static final GatewayV3ApiClient gatewayV3client = new GatewayV3ApiClient();

  @CsvSource({
      "description, ASC, Коктейль, Кофе",
      "description, DESC, Орешки, Кофе"
  })
  @ParameterizedTest
  @AllureId("200025")
  @DisplayName("REST: Список spends получен в виде Page при передаче параметров page, size c учетом сортировки по полю description")
  @Tag("REST")
  @ApiLogin(user = @GenerateUser(
      categories = @GenerateCategory(name = "Бар"),
      spends = {
          @GenerateSpend(name = "Коктейль", category = "Бар", amount = 650),
          @GenerateSpend(name = "Кофе", category = "Бар", amount = 200),
          @GenerateSpend(name = "Орешки", category = "Бар", amount = 300),
      }
  ))
  void pageableSpendsWithSortTest(String sortField,
                                  Sort.Direction direction,
                                  String firstExpected,
                                  String secondExpected,
                                  @Token String bearerToken) throws Exception {
    PagedModelJson<SpendJson> firstPage = gatewayV3client.allSpendsPageable(
        bearerToken,
        null,
        null,
        0,
        2,
        List.of(sortField + "," + direction.name())
    );
    step("Check that response not null", () ->
        assertNotNull(firstPage)
    );
    step("Check total elements count", () ->
        assertEquals(3L, firstPage.getMetadata().totalElements())
    );
    step("Check total pages", () ->
        assertEquals(2, firstPage.getMetadata().totalPages())
    );
    final List<SpendJson> firstPageContent = firstPage.getContent();

    step("Check elements size", () ->
        assertEquals(2, firstPageContent.size())
    );
    step("Check first element of first page", () ->
        assertEquals(firstExpected, firstPageContent.getFirst().description())
    );
    step("Check second element of first page", () ->
        assertEquals(secondExpected, firstPageContent.get(1).description())
    );
  }

  @Test
  @AllureId("200026")
  @DisplayName("REST: Список spends получен в виде Page при передаче параметров page, size с фильтрацией по Currency")
  @Tag("REST")
  @ApiLogin(user = @GenerateUser(
      categories = @GenerateCategory(name = "Бар"),
      spends = {
          @GenerateSpend(name = "Коктейль", category = "Бар", amount = 650, currency = CurrencyValues.RUB),
          @GenerateSpend(name = "Кофе", category = "Бар", amount = 200, currency = CurrencyValues.RUB),
          @GenerateSpend(name = "Орешки", category = "Бар", amount = 3, currency = CurrencyValues.USD),
      }
  ))
  void pageableSpendsTestWithCurrency(@Token String bearerToken) throws Exception {
    PagedModelJson<SpendJson> firstPage = gatewayV3client.allSpendsPageable(
        bearerToken,
        CurrencyValues.RUB,
        null,
        0,
        2,
        List.of("description," + Sort.Direction.ASC)
    );

    PagedModelJson<SpendJson> second = gatewayV3client.allSpendsPageable(
        bearerToken,
        CurrencyValues.RUB,
        null,
        1,
        2,
        List.of("description," + Sort.Direction.ASC)
    );
    step("Check that response not null", () ->
        assertNotNull(firstPage)
    );
    step("Check total elements count", () ->
        assertEquals(2L, firstPage.getMetadata().totalElements())
    );
    step("Check total pages", () ->
        assertEquals(1, firstPage.getMetadata().totalPages())
    );
    final List<SpendJson> firstPageContent = firstPage.getContent();

    step("Check elements size", () ->
        assertEquals(2, firstPageContent.size())
    );
    step("Check first element of first page", () ->
        assertEquals("Коктейль", firstPageContent.getFirst().description())
    );
    step("Check second element of first page", () ->
        assertEquals("Кофе", firstPageContent.get(1).description())
    );
  }

  @Test
  @AllureId("200049")
  @DisplayName("REST V3: Список spends фильтруется по searchQuery (описание траты)")
  @Tag("REST")
  @ApiLogin(user = @GenerateUser(
      categories = @GenerateCategory(name = "Бар"),
      spends = {
          @GenerateSpend(name = "Уникальный коктейль", category = "Бар", amount = 500),
          @GenerateSpend(name = "Обычный кофе", category = "Бар", amount = 150),
      }
  ))
  void spendsBySearchQueryV3Test(@Token String bearerToken) throws Exception {
    final PagedModelJson<SpendJson> result = gatewayV3client.allSpendsPageableWithSearch(
        bearerToken, null, null, 0, 10, null, "Уникальный", null
    );

    step("Check response is not null", () ->
        assertNotNull(result)
    );
    step("Check that only 1 spend matches search", () ->
        assertEquals(1, result.getContent().size())
    );
    step("Check that returned spend has correct description", () ->
        assertEquals("Уникальный коктейль", result.getContent().getFirst().description())
    );
  }

  @Test
  @AllureId("200050")
  @DisplayName("REST V3: Список spends фильтруется по категории")
  @Tag("REST")
  @ApiLogin(user = @GenerateUser(
      categories = {@GenerateCategory(name = "Бар"), @GenerateCategory(name = "Еда")},
      spends = {
          @GenerateSpend(name = "Коктейль", category = "Бар", amount = 500),
          @GenerateSpend(name = "Пицца", category = "Еда", amount = 700),
          @GenerateSpend(name = "Вино", category = "Бар", amount = 300),
      }
  ))
  void spendsByCategoryFilterV3Test(@Token String bearerToken) throws Exception {
    final PagedModelJson<SpendJson> result = gatewayV3client.allSpendsPageableWithSearch(
        bearerToken, null, null, 0, 10, null, null, "Бар"
    );

    step("Check response is not null", () ->
        assertNotNull(result)
    );
    step("Check that only 2 spends in Бар category are returned", () ->
        assertEquals(2, result.getContent().size())
    );
    step("Check all returned spends belong to Бар category", () ->
        result.getContent().forEach(s ->
            assertEquals("Бар", s.category().name())
        )
    );
  }

  @Test
  @AllureId("200051")
  @DisplayName("REST V3: GET /api/v3/spends/export/csv возвращает CSV с данными трат")
  @Tag("REST")
  @ApiLogin(user = @GenerateUser(
      categories = @GenerateCategory(name = "Бар"),
      spends = {
          @GenerateSpend(name = "Коктейль", category = "Бар", amount = 500),
      }
  ))
  void exportSpendsCsvV3Test(@Token String bearerToken) throws Exception {
    final ResponseBody body = gatewayV3client.exportSpendsCsv(bearerToken);

    step("Check response body is not null", () ->
        assertNotNull(body)
    );
    final String csv = body.string();
    step("Check CSV content is not empty", () ->
        assertFalse(csv.isEmpty())
    );
    step("Check CSV contains spend description", () ->
        assertTrue(csv.contains("Коктейль"))
    );
  }
}
