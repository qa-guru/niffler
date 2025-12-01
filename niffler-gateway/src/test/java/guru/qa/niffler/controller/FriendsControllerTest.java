package guru.qa.niffler.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FriendsControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Nested
  class FriendsControllerValidationTest {

    static Stream<Arguments> allValidationErrorsShouldBePresentInResponseBody() {
      return Stream.of(
          Arguments.of(randomAlphanumeric(2)),
          Arguments.of(randomAlphanumeric(51)),
          Arguments.of((String) null)
      );
    }

    @MethodSource
    @ParameterizedTest
    void allValidationErrorsShouldBePresentInResponseBody(String incorrectUsername) throws Exception {
      final String fixtureUser = "bee";
      mockMvc.perform(delete("/api/friends/remove")
              .with(jwt().jwt(c -> c.claim("sub", fixtureUser)))
              .param("username", incorrectUsername)
          )
          .andExpect(status().isBadRequest());
    }
  }
}