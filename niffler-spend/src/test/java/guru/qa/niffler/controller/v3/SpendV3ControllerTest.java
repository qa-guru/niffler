package guru.qa.niffler.controller.v3;

import com.opencsv.CSVReader;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SpendV3ControllerTest {

  private static final String FIXTURE = "/guru/qa/niffler/controller/v3/SpendV3ControllerTest.sql";

  private static final Matcher<String> idMatcher = Matchers.matchesPattern(
      "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
  );

  @Autowired
  private MockMvc mockMvc;

  @Test
  @Sql(FIXTURE)
  void spendsShouldBeReturnedInPagedModelStructure() throws Exception {
    final String fixtureUser = "duck";

    mockMvc.perform(get("/internal/v3/spends/all")
            .param("username", fixtureUser)
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.content[0].id").value(idMatcher))
        .andExpect(jsonPath("$.content[0].category.id").value(idMatcher))
        .andExpect(jsonPath("$.content[0].spendDate").value("2024-09-05T00:00:00.000+00:00"))
        .andExpect(jsonPath("$.content[0].category.name").value("Веселье"))
        .andExpect(jsonPath("$.content[0].category.username").value("duck"))
        .andExpect(jsonPath("$.content[0].category.archived").value(false))
        .andExpect(jsonPath("$.content[0].currency").value("RUB"))
        .andExpect(jsonPath("$.content[0].amount").value(84550.0))
        .andExpect(jsonPath("$.content[0].description").value("Обучение Niffler Next Generation!"))
        .andExpect(jsonPath("$.content[0].username").value("duck"))
        .andExpect(jsonPath("$.page.size").value(10))
        .andExpect(jsonPath("$.page.number").value(0))
        .andExpect(jsonPath("$.page.totalElements").value(1))
        .andExpect(jsonPath("$.page.totalPages").value(1));
  }

  @Test
  @Sql(FIXTURE)
  void spendsExportSpendsInCsv() throws Exception {
    final String fixtureUser = "duck";

    MockHttpServletResponse response = mockMvc.perform(get("/internal/v3/spends/export/csv")
            .param("username", fixtureUser))
        .andExpect(status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=spend-history.csv"))
        .andExpect(content().contentType("text/csv"))
        .andReturn()
        .getResponse();

    try (CSVReader reader = new CSVReader(
        new InputStreamReader(
            new ByteArrayInputStream(response.getContentAsByteArray()), StandardCharsets.UTF_8))) {

      final List<String[]> rows = reader.readAll();
      assertThat(rows, hasSize(2));
      final String[] header = rows.get(0);
      assertThat(header, arrayContaining("Id", "Category", "Description", "Amount", "Currency", "Date"));
      final String[] firstRow = rows.get(1);
      assertThat(firstRow[0], idMatcher);
      assertThat(firstRow[1], equalTo("Веселье"));
      assertThat(firstRow[2], equalTo("Обучение Niffler Next Generation!"));
      assertThat(firstRow[3], equalTo("84550.0"));
      assertThat(firstRow[4], equalTo("RUB"));
      assertThat(firstRow[5], equalTo("2024-09-05 00:00:00.0"));
    }
  }
}