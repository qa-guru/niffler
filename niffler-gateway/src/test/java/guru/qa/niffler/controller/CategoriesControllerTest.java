package guru.qa.niffler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.model.CategoryJson;
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
class CategoriesControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper om;

  @Nested
  class CategoriesControllerValidationTest {

    static Stream<Arguments> allValidationErrorsShouldBePresentInResponseBody() {
      return Stream.of(
          Arguments.of(post("/api/categories/add"), null, randomAlphanumeric(1)),
          Arguments.of(patch("/api/categories/update"), UUID.randomUUID(), null)
      );
    }

    @MethodSource
    @ParameterizedTest
    void allValidationErrorsShouldBePresentInResponseBody(MockHttpServletRequestBuilder action,
                                                          UUID categoryId,
                                                          String categoryName) throws Exception {
      final CategoryJson incorrectCategory = new CategoryJson(
          categoryId,
          categoryName,
          null,
          false
      );

      final String fixtureUser = "bee";
      mockMvc.perform(action
              .with(jwt().jwt(c -> c.claim("sub", fixtureUser)))
              .contentType(APPLICATION_JSON)
              .content(om.writeValueAsString(incorrectCategory))
          )
          .andExpect(status().isBadRequest());
    }
  }

}