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

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserV2ControllerTest {

  private static final String FIXTURE = "/guru/qa/niffler/controller/v2/UserV2ControllerTest.sql";
  private static final String SORT_FIXTURE = "/guru/qa/niffler/controller/UserControllerSecondarySort.sql";

  private static final Matcher<String> idMatcher = Matchers.matchesPattern(
      "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
  );

  @Autowired
  private MockMvc mockMvc;

  @Test
  @Sql(FIXTURE)
  void allUsersShouldBeReturnedInPagedStructure() throws Exception {
    mockMvc.perform(get("/internal/v2/users/all")
            .param("username", "duck")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(2))
        .andExpect(jsonPath("$.content[*].username", hasItem("barsik")))
        .andExpect(jsonPath("$.content[*].username", hasItem("bee")))
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(jsonPath("$.totalPages").value(1))
        .andExpect(jsonPath("$.first").value(true))
        .andExpect(jsonPath("$.last").value(true));
  }

  @Test
  @Sql(FIXTURE)
  void allUsersWithSearchQueryShouldFilterResults() throws Exception {
    mockMvc.perform(get("/internal/v2/users/all")
            .param("username", "duck")
            .param("searchQuery", "bar")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.content[0].username").value("barsik"))
        .andExpect(jsonPath("$.totalElements").value(1));
  }

  @Test
  @Sql(SORT_FIXTURE)
  void allUsersSortedByUsernameAscShouldAlwaysShowPendingBeforeNullStatus() throws Exception {
    mockMvc.perform(get("/internal/v2/users/all")
            .param("username", "duck")
            .param("sort", "username,asc")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(3))
        .andExpect(jsonPath("$.content[0].username").value("bee"))
        .andExpect(jsonPath("$.content[0].friendshipStatus").value("INVITE_SENT"))
        .andExpect(jsonPath("$.content[1].username").value("anna"))
        .andExpect(jsonPath("$.content[1].friendshipStatus").doesNotExist())
        .andExpect(jsonPath("$.content[2].username").value("zara"))
        .andExpect(jsonPath("$.content[2].friendshipStatus").doesNotExist());
  }

  @Test
  @Sql(SORT_FIXTURE)
  void allUsersSortedByUsernameDescShouldAlwaysShowPendingBeforeNullStatus() throws Exception {
    mockMvc.perform(get("/internal/v2/users/all")
            .param("username", "duck")
            .param("sort", "username,desc")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(3))
        .andExpect(jsonPath("$.content[0].username").value("bee"))
        .andExpect(jsonPath("$.content[0].friendshipStatus").value("INVITE_SENT"))
        .andExpect(jsonPath("$.content[1].username").value("zara"))
        .andExpect(jsonPath("$.content[1].friendshipStatus").doesNotExist())
        .andExpect(jsonPath("$.content[2].username").value("anna"))
        .andExpect(jsonPath("$.content[2].friendshipStatus").doesNotExist());
  }

  @Test
  @Sql(SORT_FIXTURE)
  void allUsersStatusSortPassedByUserCannotOverridePrimaryBusinessSort() throws Exception {
    mockMvc.perform(get("/internal/v2/users/all")
            .param("username", "duck")
            .param("sort", "status,desc")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].friendshipStatus").value("INVITE_SENT"));
  }

  @Test
  void allUsersForUserWithNoPeersShouldReturnEmptyPage() throws Exception {
    mockMvc.perform(get("/internal/v2/users/all")
            .param("username", "lonelyUser")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(0))
        .andExpect(jsonPath("$.totalElements").value(0));
  }

  @Test
  @Sql(FIXTURE)
  void paginationShouldSplitResultsAcrossPages() throws Exception {
    mockMvc.perform(get("/internal/v2/users/all")
            .param("username", "duck")
            .param("page", "0")
            .param("size", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(jsonPath("$.totalPages").value(2))
        .andExpect(jsonPath("$.first").value(true))
        .andExpect(jsonPath("$.last").value(false));

    mockMvc.perform(get("/internal/v2/users/all")
            .param("username", "duck")
            .param("page", "1")
            .param("size", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(jsonPath("$.first").value(false))
        .andExpect(jsonPath("$.last").value(true));
  }
}
