package guru.qa.niffler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SpendControllerTest {

  private static final String FIXTURE = "/guru/qa/niffler/controller/SpendControllerTest.sql";
  private static final String FIXTURE_SPEND_ID = "5a028ef0-4e26-4ee3-8b5f-6e5c26a9f2e9";

  private static final Matcher<String> idMatcher = Matchers.matchesPattern(
      "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
  );

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper om;

  @Test
  @Sql(FIXTURE)
  void getSpendByIdShouldReturn200WithCorrectBody() throws Exception {
    mockMvc.perform(get("/internal/spends/" + FIXTURE_SPEND_ID)
            .param("username", "duck"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(FIXTURE_SPEND_ID))
        .andExpect(jsonPath("$.username").value("duck"))
        .andExpect(jsonPath("$.currency").value("RUB"))
        .andExpect(jsonPath("$.amount").value(1500.0))
        .andExpect(jsonPath("$.description").value("Ужин"))
        .andExpect(jsonPath("$.category.name").value("Еда"));
  }

  @Test
  void getSpendByIdShouldReturn404WhenNotFound() throws Exception {
    mockMvc.perform(get("/internal/spends/00000000-0000-0000-0000-000000000000")
            .param("username", "duck"))
        .andExpect(status().isNotFound());
  }

  @Test
  void getSpendByIdShouldReturn404ForMalformedUuid() throws Exception {
    mockMvc.perform(get("/internal/spends/not-a-uuid")
            .param("username", "duck"))
        .andExpect(status().isNotFound());
  }

  @Test
  @Sql(FIXTURE)
  void getAllSpendsShouldReturn200WithListForUser() throws Exception {
    mockMvc.perform(get("/internal/spends/all")
            .param("username", "duck"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].username").value("duck"))
        .andExpect(jsonPath("$[1].username").value("duck"));
  }

  @Test
  @Sql(FIXTURE)
  void getAllSpendsWithCurrencyFilterShouldReturnOnlyMatchingCurrency() throws Exception {
    mockMvc.perform(get("/internal/spends/all")
            .param("username", "duck")
            .param("filterCurrency", "RUB"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].currency").value("RUB"));
  }

  @Test
  void getAllSpendsForUserWithNoSpendsShouldReturnEmptyList() throws Exception {
    mockMvc.perform(get("/internal/spends/all")
            .param("username", "emptyUser"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  void addSpendShouldReturn201WithCreatedSpend() throws Exception {
    SpendJson newSpend = new SpendJson(
        null,
        new Date(),
        new CategoryJson(null, "Транспорт", "duck", false),
        CurrencyValues.RUB,
        350.0,
        "Такси",
        "duck"
    );

    mockMvc.perform(post("/internal/spends/add")
            .contentType(APPLICATION_JSON)
            .content(om.writeValueAsString(newSpend)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(idMatcher))
        .andExpect(jsonPath("$.username").value("duck"))
        .andExpect(jsonPath("$.amount").value(350.0))
        .andExpect(jsonPath("$.description").value("Такси"))
        .andExpect(jsonPath("$.currency").value("RUB"))
        .andExpect(jsonPath("$.category.name").value("Транспорт"))
        .andExpect(jsonPath("$.category.archived").value(false));
  }

  @Test
  @Sql(FIXTURE)
  void editSpendShouldReturn200WithUpdatedFields() throws Exception {
    SpendJson updated = new SpendJson(
        java.util.UUID.fromString(FIXTURE_SPEND_ID),
        new Date(),
        new CategoryJson(null, "Еда", "duck", false),
        CurrencyValues.EUR,
        999.0,
        "Обновлённый ужин",
        "duck"
    );

    mockMvc.perform(patch("/internal/spends/edit")
            .contentType(APPLICATION_JSON)
            .content(om.writeValueAsString(updated)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(FIXTURE_SPEND_ID))
        .andExpect(jsonPath("$.amount").value(999.0))
        .andExpect(jsonPath("$.description").value("Обновлённый ужин"))
        .andExpect(jsonPath("$.currency").value("EUR"));
  }

  @Test
  void editSpendShouldReturn404WhenSpendNotFound() throws Exception {
    SpendJson notExisting = new SpendJson(
        java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"),
        new Date(),
        new CategoryJson(null, "Еда", "duck", false),
        CurrencyValues.RUB,
        100.0,
        "Нет такого",
        "duck"
    );

    mockMvc.perform(patch("/internal/spends/edit")
            .contentType(APPLICATION_JSON)
            .content(om.writeValueAsString(notExisting)))
        .andExpect(status().isNotFound());
  }

  @Test
  @Sql(FIXTURE)
  void getAllSpendsWithDateRangeFilterShouldReturnOnlyMatchingSpends() throws Exception {
    mockMvc.perform(get("/internal/spends/all")
            .param("username", "duck")
            .param("from", "2024-10-15")
            .param("to", "2024-10-15"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].amount").value(1500.0));
  }

  @Test
  @Sql(FIXTURE)
  void deleteSpendsShouldReturn202() throws Exception {
    mockMvc.perform(delete("/internal/spends/remove")
            .param("username", "duck")
            .param("ids", FIXTURE_SPEND_ID))
        .andExpect(status().isAccepted());
  }
}
