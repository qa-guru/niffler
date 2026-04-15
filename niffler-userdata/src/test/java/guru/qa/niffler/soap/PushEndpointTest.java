package guru.qa.niffler.soap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;

import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PushEndpointTest {

  private static final String FIXTURE = "/guru/qa/niffler/controller/PushTokenControllerTest.sql";

  @Autowired
  private ApplicationContext applicationContext;

  private MockWebServiceClient mockClient;

  @BeforeEach
  void setUp() {
    mockClient = MockWebServiceClient.createClient(applicationContext);
  }

  @Test
  @Sql(FIXTURE)
  void registerPushTokenRequestShouldSucceed() throws Exception {
    mockClient.sendRequest(withPayload(new StringSource(
        "<registerPushTokenRequest xmlns=\"niffler-userdata\">" +
        "  <username>duck</username>" +
        "  <token>fcm-soap-token-001</token>" +
        "  <userAgent>SoapTestDevice/1.0</userAgent>" +
        "</registerPushTokenRequest>"
    )))
    .andExpect(noFault());
  }

  @Test
  @Sql(FIXTURE)
  void registerPushTokenRequestWithSameTokenShouldUpsertSuccessfully() throws Exception {
    mockClient.sendRequest(withPayload(new StringSource(
        "<registerPushTokenRequest xmlns=\"niffler-userdata\">" +
        "  <username>duck</username>" +
        "  <token>fcm-soap-token-reuse</token>" +
        "  <userAgent>SoapTestDevice/1.0</userAgent>" +
        "</registerPushTokenRequest>"
    )))
    .andExpect(noFault());

    mockClient.sendRequest(withPayload(new StringSource(
        "<registerPushTokenRequest xmlns=\"niffler-userdata\">" +
        "  <username>duck</username>" +
        "  <token>fcm-soap-token-reuse</token>" +
        "  <userAgent>SoapTestDevice/2.0</userAgent>" +
        "</registerPushTokenRequest>"
    )))
    .andExpect(noFault());
  }
}
