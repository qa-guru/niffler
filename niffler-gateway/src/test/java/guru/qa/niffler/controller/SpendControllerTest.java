package guru.qa.niffler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SpendControllerTest {

  private static final long UNIX_EPOCH_TIME = 0L; // 1970-01-01 00:00:00 UTC

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper om;

  @Nested
  class SpendControllerValidationTest {

    static Stream<Arguments> allValidationErrorsShouldBePresentInResponseBody() {
      return Stream.of(
          Arguments.of(post("/api/spends/add"), null),
          Arguments.of(patch("/api/spends/edit"), UUID.randomUUID())
      );
    }

    @MethodSource
    @ParameterizedTest
    void allValidationErrorsShouldBePresentInResponseBody(MockHttpServletRequestBuilder action, UUID spendId) throws Exception {
      final SpendJson incorrectSpend = new SpendJson(
          spendId,
          new Date(UNIX_EPOCH_TIME - 1000),
          new CategoryJson(
              null,
              randomAlphanumeric(51),
              "bee",
              false
          ),
          null,
          0.002,
          randomAlphanumeric(2),
          "bee"
      );

      final String fixtureUser = "bee";
      mockMvc.perform(action
              .with(jwt().jwt(c -> c.claim("sub", fixtureUser)))
              .contentType(APPLICATION_JSON)
              .content(om.writeValueAsString(incorrectSpend))
          )
          .andExpect(status().isBadRequest());
    }
  }

}