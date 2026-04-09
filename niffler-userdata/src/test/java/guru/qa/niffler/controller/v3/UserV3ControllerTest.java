package guru.qa.niffler.controller.v3;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserV3ControllerTest {

  private static final String FIXTURE = "/guru/qa/niffler/controller/v3/UserV3ControllerTest.sql";
  private static final String SORT_FIXTURE = "/guru/qa/niffler/controller/UserControllerSecondarySort.sql";

  private static final Matcher<String> idMatcher = Matchers.matchesPattern(
      "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
  );

  @Autowired
  private MockMvc mockMvc;

  @Test
  @Sql(FIXTURE)
  void allUsersAndIncomeInvitationsShouldBeReturnedInPagedModelStructure() throws Exception {
    final String fixtureUser = "duck";

    mockMvc.perform(get("/internal/v3/users/all")
            .param("username", fixtureUser)
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(2))
        .andExpect(jsonPath("$.content[0].id").value(idMatcher))
        .andExpect(jsonPath("$.content[0].username").value("barsik"))
        .andExpect(jsonPath("$.content[1].id").value(idMatcher))
        .andExpect(jsonPath("$.content[1].username").value("bee"))
        .andExpect(jsonPath("$.page.size").value(10))
        .andExpect(jsonPath("$.page.number").value(0))
        .andExpect(jsonPath("$.page.totalElements").value(2))
        .andExpect(jsonPath("$.page.totalPages").value(1));
  }

  @Test
  @Sql(FIXTURE)
  void allUsersWithSearchQueryShouldFilterResults() throws Exception {
    mockMvc.perform(get("/internal/v3/users/all")
            .param("username", "duck")
            .param("searchQuery", "bar")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.content[0].username").value("barsik"))
        .andExpect(jsonPath("$.page.totalElements").value(1));
  }

  @Test
  @Sql(SORT_FIXTURE)
  void allUsersSortedByUsernameAscShouldAlwaysShowPendingBeforeNullStatus() throws Exception {
    mockMvc.perform(get("/internal/v3/users/all")
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
    mockMvc.perform(get("/internal/v3/users/all")
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
    mockMvc.perform(get("/internal/v3/users/all")
            .param("username", "duck")
            .param("sort", "status,desc")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].friendshipStatus").value("INVITE_SENT"));
  }

  @Test
  void allUsersForUserWithNoPeersShouldReturnEmptyPagedModel() throws Exception {
    mockMvc.perform(get("/internal/v3/users/all")
            .param("username", "lonelyUser")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(0))
        .andExpect(jsonPath("$.page.totalElements").value(0));
  }

}