package guru.qa.niffler.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserControllerTest {

  private static final String FIXTURE = "/guru/qa/niffler/controller/UserControllerTest.sql";

  @Autowired
  private MockMvc mockMvc;

  private static final String ALL_USERS_FIXTURE = "/guru/qa/niffler/controller/UserControllerAllUsersTest.sql";

  @Sql(FIXTURE)
  @Test
  void currentUserShouldBeReturned() throws Exception {
    mockMvc.perform(get("/internal/users/current")
            .contentType(MediaType.APPLICATION_JSON)
            .param("username", "dima")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("dima"))
        .andExpect(jsonPath("$.fullname").value("Dmitrii Tuchs"))
        .andExpect(jsonPath("$.currency").value("RUB"))
        .andExpect(jsonPath("$.photo").isNotEmpty())
        .andExpect(jsonPath("$.photoSmall").isNotEmpty());
  }

  @Test
  void currentUserShouldReturnDefaultForUnknownUser() throws Exception {
    mockMvc.perform(get("/internal/users/current")
            .param("username", "unknown_user"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("unknown_user"))
        .andExpect(jsonPath("$.currency").value("RUB"))
        .andExpect(jsonPath("$.id").doesNotExist());
  }

  @Test
  @Sql(ALL_USERS_FIXTURE)
  void allUsersShouldReturnOtherUsersExcludingFriends() throws Exception {
    mockMvc.perform(get("/internal/users/all")
            .param("username", "duck"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[*].username", hasItem("barsik")))
        .andExpect(jsonPath("$[*].username", hasItem("bee")));
  }

  @Test
  @Sql(ALL_USERS_FIXTURE)
  void allUsersWithSearchQueryShouldFilterByUsername() throws Exception {
    mockMvc.perform(get("/internal/users/all")
            .param("username", "duck")
            .param("searchQuery", "bar"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].username").value("barsik"));
  }

  @Test
  void allUsersForUserWithNoPeersShouldReturnEmptyList() throws Exception {
    mockMvc.perform(get("/internal/users/all")
            .param("username", "lonelyUser"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  @Sql(ALL_USERS_FIXTURE)
  void updateUserShouldPersistChanges() throws Exception {
    mockMvc.perform(post("/internal/users/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"duck\",\"currency\":\"USD\",\"fullname\":\"Updated Duck\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("duck"))
        .andExpect(jsonPath("$.currency").value("USD"))
        .andExpect(jsonPath("$.fullname").value("Updated Duck"));
  }

  @Test
  void updateUserShouldCreateUserIfNotExists() throws Exception {
    mockMvc.perform(post("/internal/users/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"newUser\",\"currency\":\"EUR\",\"fullname\":\"New User\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("newUser"))
        .andExpect(jsonPath("$.currency").value("EUR"));
  }
}