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
class FriendsEndpointTest {

  private static final String FIXTURE = "/guru/qa/niffler/soap/FriendsEndpointTest.sql";
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
  void friendsRequestShouldReturnFriendsAndInvitations() throws Exception {
    mockClient.sendRequest(withPayload(new StringSource(
        "<friendsRequest xmlns=\"niffler-userdata\">" +
        "  <username>duck</username>" +
        "</friendsRequest>"
    )))
    .andExpect(noFault())
    .andExpect(xpath("count(//ud:usersResponse/ud:user)", NS).evaluatesTo(2));
  }

  @Test
  @Sql(FIXTURE)
  void friendsRequestWithSearchQueryShouldFilterResults() throws Exception {
    mockClient.sendRequest(withPayload(new StringSource(
        "<friendsRequest xmlns=\"niffler-userdata\">" +
        "  <username>duck</username>" +
        "  <searchQuery>dima</searchQuery>" +
        "</friendsRequest>"
    )))
    .andExpect(noFault())
    .andExpect(xpath("count(//ud:usersResponse/ud:user)", NS).evaluatesTo(1))
    .andExpect(xpath("//ud:usersResponse/ud:user/ud:username", NS).evaluatesTo("dima"))
    .andExpect(xpath("//ud:usersResponse/ud:user/ud:friendshipStatus", NS).evaluatesTo("FRIEND"));
  }

  @Test
  @Sql(FIXTURE)
  void friendsPageRequestShouldReturnPagedResponse() throws Exception {
    mockClient.sendRequest(withPayload(new StringSource(
        "<friendsPageRequest xmlns=\"niffler-userdata\">" +
        "  <username>duck</username>" +
        "  <pageInfo>" +
        "    <page>0</page>" +
        "    <size>10</size>" +
        "  </pageInfo>" +
        "</friendsPageRequest>"
    )))
    .andExpect(noFault())
    .andExpect(xpath("//ud:usersResponse/ud:totalElements", NS).evaluatesTo(2))
    .andExpect(xpath("//ud:usersResponse/ud:totalPages", NS).evaluatesTo(1))
    .andExpect(xpath("//ud:usersResponse/ud:number", NS).evaluatesTo(0));
  }

  @Test
  @Sql(FIXTURE)
  void removeFriendRequestShouldRemoveFriendship() throws Exception {
    mockClient.sendRequest(withPayload(new StringSource(
        "<removeFriendRequest xmlns=\"niffler-userdata\">" +
        "  <username>duck</username>" +
        "  <friendToBeRemoved>dima</friendToBeRemoved>" +
        "</removeFriendRequest>"
    )))
    .andExpect(noFault());
  }
}
