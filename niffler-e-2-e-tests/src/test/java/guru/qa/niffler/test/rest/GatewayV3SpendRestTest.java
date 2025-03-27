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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Epic("[REST][niffler-gateway]: Пагинация Spends V3")
@DisplayName("[REST][niffler-gateway]: Пагинация Spends V3")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
      categories = @GenerateCategory("Бар"),
      spends = {
          @GenerateSpend(spendName = "Коктейль", spendCategory = "Бар", amount = 650),
          @GenerateSpend(spendName = "Кофе", spendCategory = "Бар", amount = 200),
          @GenerateSpend(spendName = "Орешки", spendCategory = "Бар", amount = 300),
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
      categories = @GenerateCategory("Бар"),
      spends = {
          @GenerateSpend(spendName = "Коктейль", spendCategory = "Бар", amount = 650, currency = CurrencyValues.RUB),
          @GenerateSpend(spendName = "Кофе", spendCategory = "Бар", amount = 200, currency = CurrencyValues.RUB),
          @GenerateSpend(spendName = "Орешки", spendCategory = "Бар", amount = 3, currency = CurrencyValues.USD),
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
}
