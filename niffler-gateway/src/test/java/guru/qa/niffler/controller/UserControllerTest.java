package guru.qa.niffler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper om;

  @Nested
  class UserControllerValidationTest {

    @Test
    void allValidationErrorsShouldBePresentInResponseBody() throws Exception {
      final UserJson incorrectUser = new UserJson(
          null,
          randomAlphanumeric(2),
          randomAlphanumeric(31),
          randomAlphanumeric(51),
          randomAlphanumeric(101),
          null,
          "data:image/svg;base64,SGVsbG8=",
          "data:image/svg;base64,SGVsbG8=",
          null
      );

      final String fixtureUser = "bee";
      mockMvc.perform(post("/api/users/update")
              .with(jwt().jwt(c -> c.claim("sub", fixtureUser)))
              .contentType(APPLICATION_JSON)
              .content(om.writeValueAsString(incorrectUser))
          )
          .andExpect(status().isBadRequest());
    }
  }
}