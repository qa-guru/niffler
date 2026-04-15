package guru.qa.niffler.controller;

import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.OauthSessionValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoginControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private OauthSessionValidator sessionValidator;

  @MockitoBean
  private KafkaAdmin kafkaAdmin;

  @MockitoBean
  private KafkaTemplate<String, UserJson> kafkaTemplate;

  @Test
  void getLoginWithWebOauthSessionShouldReturn200WithLoginView() throws Exception {
    when(sessionValidator.isWebOauthSession(any())).thenReturn(true);
    when(sessionValidator.isAndroidOauthSession(any())).thenReturn(false);

    mockMvc.perform(get("/login"))
        .andExpect(status().isOk())
        .andExpect(view().name("login"))
        .andExpect(model().attributeExists("authUri"))
        .andExpect(model().attributeExists("frontUri"));
  }

  @Test
  void getLoginWithAndroidOauthSessionShouldReturn200WithLoginView() throws Exception {
    when(sessionValidator.isWebOauthSession(any())).thenReturn(false);
    when(sessionValidator.isAndroidOauthSession(any())).thenReturn(true);

    mockMvc.perform(get("/login"))
        .andExpect(status().isOk())
        .andExpect(view().name("login"))
        .andExpect(model().attributeExists("authUri"))
        .andExpect(model().attributeExists("frontUri"));
  }

  @Test
  void getLoginWithNoOauthSessionShouldRedirectToFrontUri() throws Exception {
    when(sessionValidator.isWebOauthSession(any())).thenReturn(false);
    when(sessionValidator.isAndroidOauthSession(any())).thenReturn(false);

    mockMvc.perform(get("/login"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("http://localhost:3000"));
  }

  @Test
  void getLoginWithSessionShouldSetCorrectAuthUriFromConfig() throws Exception {
    when(sessionValidator.isWebOauthSession(any())).thenReturn(true);
    when(sessionValidator.isAndroidOauthSession(any())).thenReturn(false);

    mockMvc.perform(get("/login"))
        .andExpect(model().attribute("authUri", "http://localhost:9000"))
        .andExpect(model().attribute("frontUri", "http://localhost:3000"));
  }

  @Test
  void getRootWithoutAuthenticationShouldRedirectToLogin() throws Exception {
    mockMvc.perform(get("/"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("**/login"));
  }

  @Test
  @WithMockUser
  void getRootWithAuthenticatedUserShouldRedirectToFrontUri() throws Exception {
    mockMvc.perform(get("/"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("http://localhost:3000"));
  }

  @Test
  void getLoginWithNewSessionShouldNotExposeSecurityContextInfo() throws Exception {
    when(sessionValidator.isWebOauthSession(any())).thenReturn(true);

    mockMvc.perform(get("/login").session(new MockHttpSession()))
        .andExpect(status().isOk())
        .andExpect(view().name("login"));
  }
}
