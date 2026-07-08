package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import javax.annotation.ParametersAreNonnullByDefault;

@Epic("[REST][niffler-gateway]: Categories")
@DisplayName("[REST][niffler-gateway]: Categories")
@ParametersAreNonnullByDefault
public class GatewayCategoriesRestTest extends BaseRestTest {

  @Test
  @AllureId("200030")
  @DisplayName("REST: GET /api/categories/all возвращает все категории включая архивные")
  @Tag("REST")
  @ApiLogin(user = @GenerateUser(
      categories = {
          @GenerateCategory(name = "Бар"),
          @GenerateCategory(name = "Архив", archived = true)
      }
  ))
  void getAllCategoriesIncludingArchivedTest(@User UserJson user,
                                             @Token String bearerToken) throws Exception {
    final List<CategoryJson> categories = gatewayApiClient.allCategories(bearerToken, false);

    step("Check that categories list is not null", () ->
        assertNotNull(categories)
    );
    step("Check that response contains both categories (active + archived)", () ->
        assertEquals(2, categories.size())
    );
    step("Check that category names match", () -> {
      List<String> names = categories.stream().map(CategoryJson::name).toList();
      assertTrue(names.contains("Бар"));
      assertTrue(names.contains("Архив"));
    });
  }

  @Test
  @AllureId("200031")
  @DisplayName("REST: GET /api/categories/all?excludeArchived=true не возвращает архивные категории")
  @Tag("REST")
  @ApiLogin(user = @GenerateUser(
      categories = {
          @GenerateCategory(name = "Еда"),
          @GenerateCategory(name = "Архив", archived = true)
      }
  ))
  void getAllCategoriesExcludeArchivedTest(@Token String bearerToken) throws Exception {
    final List<CategoryJson> categories = gatewayApiClient.allCategories(bearerToken, true);

    step("Check that categories list is not null", () ->
        assertNotNull(categories)
    );
    step("Check that only active category is returned", () ->
        assertEquals(1, categories.size())
    );
    step("Check that active category name is correct", () ->
        assertEquals("Еда", categories.getFirst().name())
    );
  }

  @Test
  @AllureId("200032")
  @DisplayName("REST: POST /api/categories/add создаёт категорию и возвращает её с id")
  @Tag("REST")
  @ApiLogin(user = @GenerateUser)
  void addCategoryTest(@User UserJson user, @Token String bearerToken) throws Exception {
    final CategoryJson toCreate = new CategoryJson(null, "Спорт", user.username(), false);

    final CategoryJson created = gatewayApiClient.addCategory(bearerToken, toCreate);

    step("Check that response is not null", () ->
        assertNotNull(created)
    );
    step("Check that response contains ID (GUID)", () ->
        assertTrue(created.id().toString().matches(ID_REGEXP))
    );
    step("Check that response contains correct name", () ->
        assertEquals("Спорт", created.name())
    );
    step("Check that response contains correct username", () ->
        assertEquals(user.username(), created.username())
    );
    step("Check that category is not archived", () ->
        assertTrue(!created.archived())
    );
  }

  @Test
  @AllureId("200033")
  @DisplayName("REST: PATCH /api/categories/update архивирует существующую категорию")
  @Tag("REST")
  @ApiLogin(user = @GenerateUser(
      categories = @GenerateCategory(name = "Путешествия")
  ))
  void archiveCategoryTest(@User UserJson user, @Token String bearerToken) throws Exception {
    final CategoryJson existing = user.testData().categories().getFirst();
    final CategoryJson toArchive = new CategoryJson(existing.id(), existing.name(), user.username(), true);

    final CategoryJson updated = gatewayApiClient.updateCategory(bearerToken, toArchive);

    step("Check that response is not null", () ->
        assertNotNull(updated)
    );
    step("Check that category is now archived", () ->
        assertTrue(updated.archived())
    );
    step("Check that category name is preserved", () ->
        assertEquals("Путешествия", updated.name())
    );
  }
}
