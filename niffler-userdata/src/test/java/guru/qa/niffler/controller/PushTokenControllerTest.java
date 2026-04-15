package guru.qa.niffler.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PushTokenControllerTest {

  private static final String FIXTURE = "/guru/qa/niffler/controller/PushTokenControllerTest.sql";

  @Autowired
  private MockMvc mockMvc;

  @Test
  @Sql(FIXTURE)
  void registerPushTokenShouldReturn204() throws Exception {
    mockMvc.perform(post("/internal/push/token")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"duck\",\"token\":\"test-fcm-token-12345\",\"userAgent\":\"TestDevice/1.0\"}"))
        .andExpect(status().isNoContent());
  }

  @Test
  @Sql(FIXTURE)
  void registerPushTokenTwiceShouldUpsertAndReturn204() throws Exception {
    mockMvc.perform(post("/internal/push/token")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"duck\",\"token\":\"token-v1\",\"userAgent\":\"TestDevice/1.0\"}"))
        .andExpect(status().isNoContent());

    mockMvc.perform(post("/internal/push/token")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"duck\",\"token\":\"token-v1\",\"userAgent\":\"TestDevice/2.0\"}"))
        .andExpect(status().isNoContent());
  }
}
