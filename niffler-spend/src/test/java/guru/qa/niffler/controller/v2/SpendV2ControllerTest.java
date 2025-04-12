package guru.qa.niffler.controller.v2;

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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SpendV2ControllerTest {

  private static final String FIXTURE = "/guru/qa/niffler/controller/v2/SpendV2ControllerTest.sql";

  private static final Matcher<String> idMatcher = Matchers.matchesPattern(
      "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
  );

  @Autowired
  private MockMvc mockMvc;

  @Test
  @Sql(FIXTURE)
  void spendsShouldBeReturnedInPagedModelStructure() throws Exception {
    final String fixtureUser = "duck";

    mockMvc.perform(get("/internal/v2/spends/all")
            .param("username", fixtureUser)
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].id").value(idMatcher))
        .andExpect(jsonPath("$.content[0].spendDate").value("2024-09-04T19:00:00.000+00:00"))
        .andExpect(jsonPath("$.content[0].category.id").value(idMatcher))
        .andExpect(jsonPath("$.content[0].category.name").value("Веселье"))
        .andExpect(jsonPath("$.content[0].category.username").value("duck"))
        .andExpect(jsonPath("$.content[0].category.archived").value(false))
        .andExpect(jsonPath("$.content[0].currency").value("RUB"))
        .andExpect(jsonPath("$.content[0].amount").value(84550.0))
        .andExpect(jsonPath("$.content[0].description").value("Обучение Niffler Next Generation!"))
        .andExpect(jsonPath("$.content[0].username").value("duck"))
        .andExpect(jsonPath("$.pageable.pageNumber").value(0))
        .andExpect(jsonPath("$.pageable.pageSize").value(10))
        .andExpect(jsonPath("$.pageable.offset").value(0))
        .andExpect(jsonPath("$.pageable.paged").value(true))
        .andExpect(jsonPath("$.pageable.unpaged").value(false))
        .andExpect(jsonPath("$.pageable.sort.sorted").value(false))
        .andExpect(jsonPath("$.pageable.sort.unsorted").value(true))
        .andExpect(jsonPath("$.pageable.sort.empty").value(true))
        .andExpect(jsonPath("$.sort.sorted").value(false))
        .andExpect(jsonPath("$.sort.unsorted").value(true))
        .andExpect(jsonPath("$.sort.empty").value(true))
        .andExpect(jsonPath("$.first").value(true))
        .andExpect(jsonPath("$.last").value(true))
        .andExpect(jsonPath("$.number").value(0))
        .andExpect(jsonPath("$.size").value(10))
        .andExpect(jsonPath("$.numberOfElements").value(1))
        .andExpect(jsonPath("$.totalElements").value(1))
        .andExpect(jsonPath("$.totalPages").value(1))
        .andExpect(jsonPath("$.empty").value(false));
  }
}