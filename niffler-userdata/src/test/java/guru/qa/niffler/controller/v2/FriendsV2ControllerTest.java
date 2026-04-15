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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class FriendsV2ControllerTest {

  private static final String FIXTURE = "/guru/qa/niffler/controller/v2/FriendsV2ControllerTest.sql";
  private static final String SORT_FIXTURE = "/guru/qa/niffler/controller/FriendsControllerSecondarySort.sql";

  private static final Matcher<String> idMatcher = Matchers.matchesPattern(
      "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
  );

  @Autowired
  private MockMvc mockMvc;

  @Test
  @Sql(FIXTURE)
  void friendsShouldBeReturnedInPagedStructure() throws Exception {
    mockMvc.perform(get("/internal/v2/friends/all")
            .param("username", "duck")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(2))
        .andExpect(jsonPath("$.content[0].id").value(idMatcher))
        .andExpect(jsonPath("$.content[0].username").value("bee"))
        .andExpect(jsonPath("$.content[0].friendshipStatus").value("INVITE_RECEIVED"))
        .andExpect(jsonPath("$.content[1].username").value("dima"))
        .andExpect(jsonPath("$.content[1].friendshipStatus").value("FRIEND"))
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(jsonPath("$.totalPages").value(1))
        .andExpect(jsonPath("$.first").value(true))
        .andExpect(jsonPath("$.last").value(true));
  }

  @Test
  @Sql(FIXTURE)
  void friendsWithSearchQueryShouldFilterResults() throws Exception {
    mockMvc.perform(get("/internal/v2/friends/all")
            .param("username", "duck")
            .param("searchQuery", "dima")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.content[0].username").value("dima"))
        .andExpect(jsonPath("$.content[0].friendshipStatus").value("FRIEND"))
        .andExpect(jsonPath("$.totalElements").value(1));
  }

  @Test
  @Sql(SORT_FIXTURE)
  void friendsSortedByUsernameAscShouldAlwaysShowInviteReceivedBeforeFriend() throws Exception {
    mockMvc.perform(get("/internal/v2/friends/all")
            .param("username", "duck")
            .param("sort", "username,asc")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(3))
        .andExpect(jsonPath("$.content[0].username").value("bee"))
        .andExpect(jsonPath("$.content[0].friendshipStatus").value("INVITE_RECEIVED"))
        .andExpect(jsonPath("$.content[1].username").value("anna"))
        .andExpect(jsonPath("$.content[1].friendshipStatus").value("FRIEND"))
        .andExpect(jsonPath("$.content[2].username").value("zara"))
        .andExpect(jsonPath("$.content[2].friendshipStatus").value("FRIEND"));
  }

  @Test
  @Sql(SORT_FIXTURE)
  void friendsSortedByUsernameDescShouldAlwaysShowInviteReceivedBeforeFriend() throws Exception {
    mockMvc.perform(get("/internal/v2/friends/all")
            .param("username", "duck")
            .param("sort", "username,desc")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(3))
        .andExpect(jsonPath("$.content[0].username").value("bee"))
        .andExpect(jsonPath("$.content[0].friendshipStatus").value("INVITE_RECEIVED"))
        .andExpect(jsonPath("$.content[1].username").value("zara"))
        .andExpect(jsonPath("$.content[1].friendshipStatus").value("FRIEND"))
        .andExpect(jsonPath("$.content[2].username").value("anna"))
        .andExpect(jsonPath("$.content[2].friendshipStatus").value("FRIEND"));
  }

  @Test
  @Sql(SORT_FIXTURE)
  void friendsStatusSortPassedByUserCannotOverridePrimaryBusinessSort() throws Exception {
    mockMvc.perform(get("/internal/v2/friends/all")
            .param("username", "duck")
            .param("sort", "status,asc")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].friendshipStatus").value("INVITE_RECEIVED"));
  }

  @Test
  void friendsForUserWithNoConnectionsShouldReturn404() throws Exception {
    mockMvc.perform(get("/internal/v2/friends/all")
            .param("username", "lonelyUser")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isNotFound());
  }

  @Test
  @Sql(FIXTURE)
  void paginationShouldSplitFriendsAcrossPages() throws Exception {
    mockMvc.perform(get("/internal/v2/friends/all")
            .param("username", "duck")
            .param("page", "0")
            .param("size", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(jsonPath("$.totalPages").value(2))
        .andExpect(jsonPath("$.first").value(true))
        .andExpect(jsonPath("$.last").value(false));

    mockMvc.perform(get("/internal/v2/friends/all")
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
