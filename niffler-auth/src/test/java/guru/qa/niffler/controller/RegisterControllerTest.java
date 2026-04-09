package guru.qa.niffler.controller;

import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "/sql/user_cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class RegisterControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private KafkaAdmin kafkaAdmin;

  @MockitoBean
  private KafkaTemplate<String, UserJson> kafkaTemplate;

  @Test
  void getRegisterShouldReturn200WithEmptyForm() throws Exception {
    mockMvc.perform(get("/register"))
        .andExpect(status().isOk())
        .andExpect(view().name("register"))
        .andExpect(model().attributeExists("registrationModel"));
  }

  @Test
  void postRegisterWithValidDataShouldReturn201AndIncludeUsernameInModel() throws Exception {
    mockMvc.perform(post("/register")
            .with(csrf())
            .param("username", "newuser")
            .param("password", "valid_pass")
            .param("passwordSubmit", "valid_pass"))
        .andExpect(status().isCreated())
        .andExpect(view().name("register"))
        .andExpect(model().attribute("username", "newuser"));
  }

  @Test
  void postRegisterWithValidDataShouldSendKafkaMessage() throws Exception {
    mockMvc.perform(post("/register")
            .with(csrf())
            .param("username", "kafkauser")
            .param("password", "valid_pass")
            .param("passwordSubmit", "valid_pass"))
        .andExpect(status().isCreated());

    verify(kafkaTemplate, times(1)).send(eq("users"), any(UserJson.class));
  }

  @Test
  @Sql("/sql/user_existing.sql")
  void postRegisterWithExistingUsernameShouldReturn400WithUsernameError() throws Exception {
    mockMvc.perform(post("/register")
            .with(csrf())
            .param("username", "existinguser")
            .param("password", "valid_pass")
            .param("passwordSubmit", "valid_pass"))
        .andExpect(status().isBadRequest())
        .andExpect(view().name("register"))
        .andExpect(model().hasErrors());
  }

  @Test
  void postRegisterWithBlankUsernameShouldReturn400WithValidationError() throws Exception {
    mockMvc.perform(post("/register")
            .with(csrf())
            .param("username", "")
            .param("password", "valid_pass")
            .param("passwordSubmit", "valid_pass"))
        .andExpect(status().isBadRequest())
        .andExpect(view().name("register"))
        .andExpect(model().hasErrors());
  }

  @Test
  void postRegisterWithMismatchedPasswordsShouldReturn400WithValidationError() throws Exception {
    mockMvc.perform(post("/register")
            .with(csrf())
            .param("username", "validuser")
            .param("password", "password1")
            .param("passwordSubmit", "password2"))
        .andExpect(status().isBadRequest())
        .andExpect(view().name("register"))
        .andExpect(model().hasErrors());
  }

  @Test
  void postRegisterWithUsernameContainingSpaceShouldReturn400WithValidationError() throws Exception {
    mockMvc.perform(post("/register")
            .with(csrf())
            .param("username", "user name")
            .param("password", "valid_pass")
            .param("passwordSubmit", "valid_pass"))
        .andExpect(status().isBadRequest())
        .andExpect(view().name("register"))
        .andExpect(model().hasErrors());
  }

  @Test
  void postRegisterWithPasswordContainingSpaceShouldReturn400WithValidationError() throws Exception {
    mockMvc.perform(post("/register")
            .with(csrf())
            .param("username", "validuser")
            .param("password", "pass word")
            .param("passwordSubmit", "pass word"))
        .andExpect(status().isBadRequest())
        .andExpect(view().name("register"))
        .andExpect(model().hasErrors());
  }

  @Test
  void postRegisterWithMissingCsrfTokenShouldReturn403() throws Exception {
    mockMvc.perform(post("/register")
            .param("username", "newuser")
            .param("password", "valid_pass")
            .param("passwordSubmit", "valid_pass"))
        .andExpect(status().isForbidden());
  }
}
