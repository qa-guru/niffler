package guru.qa.niffler.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class FriendsControllerTest {

  private static final String FIXTURE = "/guru/qa/niffler/controller/FriendsControllerTest.sql";

  @Autowired
  private MockMvc mockMvc;

  @Test
  @Sql(FIXTURE)
  void friendsShouldReturnFriendsAndIncomingInvitations() throws Exception {
    mockMvc.perform(get("/internal/friends/all")
            .param("username", "duck"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[*].username", hasItem("bee")))
        .andExpect(jsonPath("$[*].username", hasItem("dima")));
  }

  @Test
  @Sql(FIXTURE)
  void friendsWithSearchQueryShouldFilterResults() throws Exception {
    mockMvc.perform(get("/internal/friends/all")
            .param("username", "duck")
            .param("searchQuery", "bee"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].username").value("bee"))
        .andExpect(jsonPath("$[0].friendshipStatus").value("INVITE_RECEIVED"));
  }

  @Test
  @Sql(FIXTURE)
  void friendsWithNonMatchingSearchQueryShouldReturnEmpty() throws Exception {
    mockMvc.perform(get("/internal/friends/all")
            .param("username", "duck")
            .param("searchQuery", "zzz"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  @Sql(FIXTURE)
  void friendsShouldReturnFriendshipStatusCorrectly() throws Exception {
    mockMvc.perform(get("/internal/friends/all")
            .param("username", "duck"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].friendshipStatus", hasItem("INVITE_RECEIVED")))
        .andExpect(jsonPath("$[*].friendshipStatus", hasItem("FRIEND")));
  }

  @Test
  void friendsForUserWithNoFriendsShouldReturnEmptyList() throws Exception {
    mockMvc.perform(get("/internal/friends/all")
            .param("username", "lonelyUser"))
        .andExpect(status().isNotFound());
  }

  @Test
  @Sql(FIXTURE)
  void removeFriendShouldReturn200() throws Exception {
    mockMvc.perform(delete("/internal/friends/remove")
            .param("username", "duck")
            .param("targetUsername", "dima"))
        .andExpect(status().isOk());
  }

  @Test
  void removeFriendSelfShouldReturn400() throws Exception {
    mockMvc.perform(delete("/internal/friends/remove")
            .param("username", "duck")
            .param("targetUsername", "duck"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Sql(FIXTURE)
  void removeFriendNonExistentTargetShouldReturn404() throws Exception {
    mockMvc.perform(delete("/internal/friends/remove")
            .param("username", "duck")
            .param("targetUsername", "ghost"))
        .andExpect(status().isNotFound());
  }
}
