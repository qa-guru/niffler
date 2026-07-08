package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.converter.SpendConverter;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.ValueSource;

import java.text.SimpleDateFormat;
import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import javax.annotation.ParametersAreNonnullByDefault;

@Epic("[REST][niffler-gateway]: Spends")
@DisplayName("[REST][niffler-gateway]: Spends")
@ParametersAreNonnullByDefault
public class GatewaySpendRestTest extends BaseRestTest {

  private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

  @AllureId("200008")
  @ValueSource(strings = {
      "rest/spend0.json",
      "rest/spend1.json"
  })
  @DisplayName("REST: При создании нового spend возвращается ID из niffler-spend")
  @ParameterizedTest(name = "Тестовые данные для запроса: {0}")
  @ApiLogin(
      user = @GenerateUser(categories = {
          @GenerateCategory(name = "Рестораны"),
          @GenerateCategory(name = "Бары"),
      })
  )
  @Tag("REST")
  void apiShouldReturnIdOfCreatedSpend(@ConvertWith(SpendConverter.class) SpendJson spend,
                                       @User UserJson user,
                                       @Token String bearerToken) throws Exception {
    final SpendJson created = gatewayApiClient.addSpend(
        bearerToken,
        spend.addUsername(user.username())
    );

    step("Check that response contains ID (GUID)", () ->
        assertTrue(created.id().toString().matches(ID_REGEXP))
    );
    step("Check that response contains username", () ->
        assertEquals(user.username(), created.username())
    );
  }

  @Test
  @AllureId("200034")
  @DisplayName("REST: GET /api/spends/{id} возвращает трату по id")
  @Tag("REST")
  @ApiLogin(user = @GenerateUser(
      categories = @GenerateCategory(name = "Бар"),
      spends = @GenerateSpend(name = "Коктейль", category = "Бар", amount = 500)
  ))
  void getSpendByIdTest(@User UserJson user, @Token String bearerToken) throws Exception {
    final SpendJson generated = user.testData().spends().getFirst();

    final SpendJson found = gatewayApiClient.getSpend(bearerToken, generated.id().toString());

    step("Check that response is not null", () ->
        assertNotNull(found)
    );
    step("Check that id matches", () ->
        assertEquals(generated.id(), found.id())
    );
    step("Check that description matches", () ->
        assertEquals("Коктейль", found.description())
    );
    step("Check that amount matches", () ->
        assertEquals(500.0, found.amount())
    );
    step("Check that username matches", () ->
        assertEquals(user.username(), found.username())
    );
  }

  @Test
  @AllureId("200035")
  @DisplayName("REST: GET /api/spends/all возвращает список трат пользователя")
  @Tag("REST")
  @ApiLogin(user = @GenerateUser(
      categories = @GenerateCategory(name = "Бар"),
      spends = {
          @GenerateSpend(name = "Коктейль", category = "Бар", amount = 500),
          @GenerateSpend(name = "Кофе", category = "Бар", amount = 200)
      }
  ))
  void getAllSpendsTest(@Token String bearerToken) throws Exception {
    final List<SpendJson> spends = gatewayApiClient.allSpends(bearerToken, null, null);

    step("Check that response is not null", () ->
        assertNotNull(spends)
    );
    step("Check that 2 spends are returned", () ->
        assertEquals(2, spends.size())
    );
  }

  @Test
  @AllureId("200036")
  @DisplayName("REST: PATCH /api/spends/edit обновляет трату")
  @Tag("REST")
  @ApiLogin(user = @GenerateUser(
      categories = @GenerateCategory(name = "Бар"),
      spends = @GenerateSpend(name = "Пиво", category = "Бар", amount = 100)
  ))
  void editSpendTest(@User UserJson user, @Token String bearerToken) throws Exception {
    final SpendJson original = user.testData().spends().getFirst();
    final CategoryJson category = user.testData().categories().getFirst();
    final SpendJson updated = new SpendJson(
        original.id(),
        original.spendDate(),
        original.amount() + 50.0,
        CurrencyValues.RUB,
        category,
        "Крафтовое пиво",
        user.username()
    );

    final SpendJson result = gatewayApiClient.editSpend(bearerToken, updated);

    step("Check that response is not null", () ->
        assertNotNull(result)
    );
    step("Check that description is updated", () ->
        assertEquals("Крафтовое пиво", result.description())
    );
    step("Check that amount is updated", () ->
        assertEquals(150.0, result.amount())
    );
  }

  @Test
  @AllureId("200037")
  @DisplayName("REST: DELETE /api/spends/remove удаляет траты")
  @Tag("REST")
  @ApiLogin(user = @GenerateUser(
      categories = @GenerateCategory(name = "Бар"),
      spends = {
          @GenerateSpend(name = "Коктейль", category = "Бар", amount = 500),
          @GenerateSpend(name = "Кофе", category = "Бар", amount = 200)
      }
  ))
  void removeSpendsTest(@User UserJson user, @Token String bearerToken) throws Exception {
    final List<String> ids = user.testData().spends().stream()
        .map(s -> s.id().toString())
        .toList();

    gatewayApiClient.removeSpends(bearerToken, ids);

    final List<SpendJson> remaining = gatewayApiClient.allSpends(bearerToken, null, null);

    step("Check that spend list is empty after removal", () ->
        assertTrue(remaining == null || remaining.isEmpty())
    );
  }
}
