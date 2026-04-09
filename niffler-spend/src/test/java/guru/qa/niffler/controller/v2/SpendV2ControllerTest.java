package guru.qa.niffler.controller.v2;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.StringStartsWith;
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

  private static final String MULTI_PAGE_FIXTURE = "/guru/qa/niffler/controller/v2/SpendV2ControllerTest_MultiPage.sql";

  @Test
  @Sql(MULTI_PAGE_FIXTURE)
  void spendsShouldBeSortedBySpendDateDescending() throws Exception {
    mockMvc.perform(get("/internal/v2/spends/all")
            .param("username", "duck")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(3)))
        .andExpect(jsonPath("$.content[0].amount").value(300.0))
        .andExpect(jsonPath("$.content[1].amount").value(200.0))
        .andExpect(jsonPath("$.content[2].amount").value(100.0));
  }

  @Test
  @Sql(MULTI_PAGE_FIXTURE)
  void paginationShouldSplitResultsAcrossPages() throws Exception {
    mockMvc.perform(get("/internal/v2/spends/all")
            .param("username", "duck")
            .param("page", "0")
            .param("size", "2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(2)))
        .andExpect(jsonPath("$.totalElements").value(3))
        .andExpect(jsonPath("$.totalPages").value(2))
        .andExpect(jsonPath("$.first").value(true))
        .andExpect(jsonPath("$.last").value(false));

    mockMvc.perform(get("/internal/v2/spends/all")
            .param("username", "duck")
            .param("page", "1")
            .param("size", "2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.totalElements").value(3))
        .andExpect(jsonPath("$.first").value(false))
        .andExpect(jsonPath("$.last").value(true));
  }

  @Test
  @Sql(MULTI_PAGE_FIXTURE)
  void customSortParameterShouldOverrideDefaultSpendDateDesc() throws Exception {
    mockMvc.perform(get("/internal/v2/spends/all")
            .param("username", "duck")
            .param("sort", "amount,asc"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].amount").value(100.0))
        .andExpect(jsonPath("$.content[1].amount").value(200.0))
        .andExpect(jsonPath("$.content[2].amount").value(300.0));
  }

  @Test
  void getSpendsForUserWithNoSpendsShouldReturnEmptyPage() throws Exception {
    mockMvc.perform(get("/internal/v2/spends/all")
            .param("username", "emptyUser")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(0)))
        .andExpect(jsonPath("$.totalElements").value(0))
        .andExpect(jsonPath("$.empty").value(true));
  }

  @Test
  @Sql(FIXTURE)
  void getSpendsWithMatchingCurrencyFilterShouldReturnResults() throws Exception {
    mockMvc.perform(get("/internal/v2/spends/all")
            .param("username", "duck")
            .param("filterCurrency", "RUB"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].currency").value("RUB"));
  }

  @Test
  @Sql(FIXTURE)
  void getSpendsWithNonMatchingCurrencyFilterShouldReturnEmptyPage() throws Exception {
    mockMvc.perform(get("/internal/v2/spends/all")
            .param("username", "duck")
            .param("filterCurrency", "USD"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(0)))
        .andExpect(jsonPath("$.totalElements").value(0));
  }

  @Test
  @Sql(FIXTURE)
  void getSpendsWithMatchingSearchQueryShouldReturnResults() throws Exception {
    mockMvc.perform(get("/internal/v2/spends/all")
            .param("username", "duck")
            .param("searchQuery", "Niffler"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].description").value("Обучение Niffler Next Generation!"));
  }

  @Test
  @Sql(FIXTURE)
  void getSpendsWithNonMatchingSearchQueryShouldReturnEmptyPage() throws Exception {
    mockMvc.perform(get("/internal/v2/spends/all")
            .param("username", "duck")
            .param("searchQuery", "несуществующий запрос xyz"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(0)));
  }

  @Test
  @Sql(FIXTURE)
  void getSpendsWithDateRangeFilterShouldReturnMatchingSpends() throws Exception {
    mockMvc.perform(get("/internal/v2/spends/all")
            .param("username", "duck")
            .param("from", "2024-09-05")
            .param("to", "2024-09-05"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)));
  }

  @Test
  @Sql(FIXTURE)
  void getSpendsOutsideDateRangeShouldReturnEmptyPage() throws Exception {
    mockMvc.perform(get("/internal/v2/spends/all")
            .param("username", "duck")
            .param("from", "2020-01-01")
            .param("to", "2020-12-31"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(0)));
  }

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
        .andExpect(jsonPath("$.content[0].spendDate").value(Matchers.matchesPattern("2024-09-0[45]T.*")))
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
        .andExpect(jsonPath("$.pageable.sort.sorted").value(true))
        .andExpect(jsonPath("$.pageable.sort.unsorted").value(false))
        .andExpect(jsonPath("$.pageable.sort.empty").value(false))
        .andExpect(jsonPath("$.sort.sorted").value(true))
        .andExpect(jsonPath("$.sort.unsorted").value(false))
        .andExpect(jsonPath("$.sort.empty").value(false))
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