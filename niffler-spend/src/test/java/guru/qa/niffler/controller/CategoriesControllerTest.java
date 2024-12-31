package guru.qa.niffler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.Test;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CategoriesControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper om;

  @Test
  @Sql("/categoriesListShouldBeReturnedForCurrentUser.sql")
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

  @Test
  @Sql("/categoryNameAndArchivedStatusShouldBeUpdated.sql")
  void categoryNameAndArchivedStatusShouldBeUpdated() throws Exception {
    final String fixtureCategoryId = "06fe9c06-ae67-406e-a711-ba729e3d4775";
    final String fixtureUser = "duck";

    CategoryJson categoryJson = new CategoryJson(
        UUID.fromString(fixtureCategoryId),
        "Бары",
        fixtureUser,
        true
    );

    mockMvc.perform(patch("/internal/categories/update")
            .contentType(APPLICATION_JSON)
            .content(om.writeValueAsString(categoryJson)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(fixtureCategoryId))
        .andExpect(jsonPath("$.name").value("Бары"))
        .andExpect(jsonPath("$.username").value(fixtureUser))
        .andExpect(jsonPath("$.archived").value(true));
  }
}