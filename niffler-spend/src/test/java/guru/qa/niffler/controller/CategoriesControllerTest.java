package guru.qa.niffler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.model.CategoryJson;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CategoriesControllerTest {

  private static final String FIXTURE = "/guru/qa/niffler/controller/CategoriesControllerTest.sql";

  private static final Matcher<String> idMatcher = Matchers.matchesPattern(
      "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
  );

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper om;

  @Test
  @Sql(FIXTURE)
  void categoriesListShouldBeReturnedForCurrentUser() throws Exception {
    final String fixtureUser = "duck";

    mockMvc.perform(get("/internal/categories/all")
            .param("username", fixtureUser))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].username").value(fixtureUser))
        .andExpect(jsonPath("$[0].name").value("Веселье"))
        .andExpect(jsonPath("$[0].archived").value(false))
        .andExpect(jsonPath("$[1].username").value(fixtureUser))
        .andExpect(jsonPath("$[1].name").value("Магазины"))
        .andExpect(jsonPath("$[1].archived").value(true));
  }

  @ValueSource(booleans = {true, false})
  @ParameterizedTest
  void shouldAddActiveCategoryForCurrentUser(boolean bodyArchivedState) throws Exception {
    final String fixtureUser = "duck";
    final String categoryName = "test category with initial state: " + bodyArchivedState;
    final CategoryJson category = new CategoryJson(
        null,
        categoryName,
        fixtureUser,
        bodyArchivedState
    );

    mockMvc.perform(post("/internal/categories/add")
            .contentType(APPLICATION_JSON)
            .content(om.writeValueAsString(category)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(idMatcher))
        .andExpect(jsonPath("$.name").value(categoryName))
        .andExpect(jsonPath("$.username").value(fixtureUser))
        .andExpect(jsonPath("$.archived").value(false));
  }

  @Test
  @Sql(FIXTURE)
  void categoryNameAndArchivedStatusShouldBeUpdated() throws Exception {
    final String fixtureCategoryId = "68bcb6da-f690-41df-bef4-18c98de97d1a";
    final String fixtureUser = "duck";

    CategoryJson categoryJson = new CategoryJson(
        UUID.fromString(fixtureCategoryId),
        "Веселье",
        fixtureUser,
        true
    );

    mockMvc.perform(patch("/internal/categories/update")
            .contentType(APPLICATION_JSON)
            .content(om.writeValueAsString(categoryJson)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(fixtureCategoryId))
        .andExpect(jsonPath("$.name").value("Веселье"))
        .andExpect(jsonPath("$.username").value(fixtureUser))
        .andExpect(jsonPath("$.archived").value(true));
  }
}