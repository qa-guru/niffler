package guru.qa.niffler.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class InvitationsControllerTest {

  private static final String FIXTURE = "/guru/qa/niffler/controller/InvitationsControllerTest.sql";

  @Autowired
  private MockMvc mockMvc;

  @Test
  @Sql(FIXTURE)
  void sendInvitationShouldReturnTargetWithInviteSentStatus() throws Exception {
    mockMvc.perform(post("/internal/invitations/send")
            .param("username", "duck")
            .param("targetUsername", "barsik"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("barsik"))
        .andExpect(jsonPath("$.friendshipStatus").value("INVITE_SENT"));
  }

  @Test
  @Sql(FIXTURE)
  void sendInvitationToUserWhoAlreadySentInviteShouldReturnFriend() throws Exception {
    mockMvc.perform(post("/internal/invitations/send")
            .param("username", "duck")
            .param("targetUsername", "bee"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("bee"))
        .andExpect(jsonPath("$.friendshipStatus").value("FRIEND"));
  }

  @Test
  void sendInvitationToSelfShouldReturn400() throws Exception {
    mockMvc.perform(post("/internal/invitations/send")
            .param("username", "duck")
            .param("targetUsername", "duck"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Sql(FIXTURE)
  void sendInvitationToNonExistentUserShouldReturn404() throws Exception {
    mockMvc.perform(post("/internal/invitations/send")
            .param("username", "duck")
            .param("targetUsername", "ghost"))
        .andExpect(status().isNotFound());
  }

  @Test
  @Sql(FIXTURE)
  void acceptInvitationShouldReturnTargetWithFriendStatus() throws Exception {
    mockMvc.perform(post("/internal/invitations/accept")
            .param("username", "duck")
            .param("targetUsername", "bee"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("bee"))
        .andExpect(jsonPath("$.friendshipStatus").value("FRIEND"));
  }

  @Test
  void acceptInvitationToSelfShouldReturn400() throws Exception {
    mockMvc.perform(post("/internal/invitations/accept")
            .param("username", "duck")
            .param("targetUsername", "duck"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Sql(FIXTURE)
  void acceptInvitationWhenNoInviteExistsShouldReturn404() throws Exception {
    mockMvc.perform(post("/internal/invitations/accept")
            .param("username", "duck")
            .param("targetUsername", "barsik"))
        .andExpect(status().isNotFound());
  }

  @Test
  @Sql(FIXTURE)
  void declineInvitationShouldReturnTargetWithNoFriendshipStatus() throws Exception {
    mockMvc.perform(post("/internal/invitations/decline")
            .param("username", "duck")
            .param("targetUsername", "bee"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("bee"))
        .andExpect(jsonPath("$.friendshipStatus").doesNotExist());
  }

  @Test
  void declineInvitationToSelfShouldReturn400() throws Exception {
    mockMvc.perform(post("/internal/invitations/decline")
            .param("username", "duck")
            .param("targetUsername", "duck"))
        .andExpect(status().isBadRequest());
  }
}
