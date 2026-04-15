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

import java.util.Map;

import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;
import static org.springframework.ws.test.server.ResponseMatchers.xpath;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class InvitationsEndpointTest {

  private static final String FIXTURE = "/guru/qa/niffler/soap/InvitationsEndpointTest.sql";
  private static final Map<String, String> NS = Map.of("ud", "niffler-userdata");

  @Autowired
  private ApplicationContext applicationContext;

  private MockWebServiceClient mockClient;

  @BeforeEach
  void setUp() {
    mockClient = MockWebServiceClient.createClient(applicationContext);
  }

  @Test
  @Sql(FIXTURE)
  void sendInvitationRequestShouldReturnTargetWithInviteSentStatus() throws Exception {
    mockClient.sendRequest(withPayload(new StringSource(
        "<sendInvitationRequest xmlns=\"niffler-userdata\">" +
        "  <username>duck</username>" +
        "  <friendToBeRequested>barsik</friendToBeRequested>" +
        "</sendInvitationRequest>"
    )))
    .andExpect(noFault())
    .andExpect(xpath("//ud:userResponse/ud:user/ud:username", NS).evaluatesTo("barsik"))
    .andExpect(xpath("//ud:userResponse/ud:user/ud:friendshipStatus", NS).evaluatesTo("INVITE_SENT"));
  }

  @Test
  @Sql(FIXTURE)
  void sendInvitationToUserWhoAlreadySentInviteShouldReturnFriend() throws Exception {
    mockClient.sendRequest(withPayload(new StringSource(
        "<sendInvitationRequest xmlns=\"niffler-userdata\">" +
        "  <username>duck</username>" +
        "  <friendToBeRequested>bee</friendToBeRequested>" +
        "</sendInvitationRequest>"
    )))
    .andExpect(noFault())
    .andExpect(xpath("//ud:userResponse/ud:user/ud:username", NS).evaluatesTo("bee"))
    .andExpect(xpath("//ud:userResponse/ud:user/ud:friendshipStatus", NS).evaluatesTo("FRIEND"));
  }

  @Test
  @Sql(FIXTURE)
  void acceptInvitationRequestShouldReturnTargetWithFriendStatus() throws Exception {
    mockClient.sendRequest(withPayload(new StringSource(
        "<acceptInvitationRequest xmlns=\"niffler-userdata\">" +
        "  <username>duck</username>" +
        "  <friendToBeAdded>bee</friendToBeAdded>" +
        "</acceptInvitationRequest>"
    )))
    .andExpect(noFault())
    .andExpect(xpath("//ud:userResponse/ud:user/ud:username", NS).evaluatesTo("bee"))
    .andExpect(xpath("//ud:userResponse/ud:user/ud:friendshipStatus", NS).evaluatesTo("FRIEND"));
  }

  @Test
  @Sql(FIXTURE)
  void declineInvitationRequestShouldSucceed() throws Exception {
    mockClient.sendRequest(withPayload(new StringSource(
        "<declineInvitationRequest xmlns=\"niffler-userdata\">" +
        "  <username>duck</username>" +
        "  <invitationToBeDeclined>bee</invitationToBeDeclined>" +
        "</declineInvitationRequest>"
    )))
    .andExpect(noFault())
    .andExpect(xpath("//ud:userResponse/ud:user/ud:username", NS).evaluatesTo("bee"));
  }
}
