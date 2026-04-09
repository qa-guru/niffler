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
class UserEndpointTest {

  private static final String FIXTURE = "/guru/qa/niffler/soap/UserEndpointTest.sql";
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
  void currentUserRequestShouldReturnUser() throws Exception {
    mockClient.sendRequest(withPayload(new StringSource(
        "<currentUserRequest xmlns=\"niffler-userdata\">" +
        "  <username>duck</username>" +
        "</currentUserRequest>"
    )))
    .andExpect(noFault())
    .andExpect(xpath("//ud:userResponse/ud:user/ud:username", NS).evaluatesTo("duck"))
    .andExpect(xpath("//ud:userResponse/ud:user/ud:currency", NS).evaluatesTo("RUB"));
  }

  @Test
  @Sql(FIXTURE)
  void updateUserRequestShouldPersistChanges() throws Exception {
    mockClient.sendRequest(withPayload(new StringSource(
        "<updateUserRequest xmlns=\"niffler-userdata\">" +
        "  <user>" +
        "    <id>08716034-690c-4a3f-8961-7b31b45896aa</id>" +
        "    <username>duck</username>" +
        "    <fullname>Updated Duck</fullname>" +
        "    <currency>USD</currency>" +
        "  </user>" +
        "</updateUserRequest>"
    )))
    .andExpect(noFault())
    .andExpect(xpath("//ud:userResponse/ud:user/ud:username", NS).evaluatesTo("duck"))
    .andExpect(xpath("//ud:userResponse/ud:user/ud:currency", NS).evaluatesTo("USD"))
    .andExpect(xpath("//ud:userResponse/ud:user/ud:fullname", NS).evaluatesTo("Updated Duck"));
  }

  @Test
  @Sql(FIXTURE)
  void allUsersRequestShouldReturnOtherUsers() throws Exception {
    mockClient.sendRequest(withPayload(new StringSource(
        "<allUsersRequest xmlns=\"niffler-userdata\">" +
        "  <username>duck</username>" +
        "</allUsersRequest>"
    )))
    .andExpect(noFault())
    .andExpect(xpath("count(//ud:usersResponse/ud:user)", NS).evaluatesTo(2));
  }

  @Test
  @Sql(FIXTURE)
  void allUsersPageRequestShouldReturnPagedResponse() throws Exception {
    mockClient.sendRequest(withPayload(new StringSource(
        "<allUsersPageRequest xmlns=\"niffler-userdata\">" +
        "  <username>duck</username>" +
        "  <pageInfo>" +
        "    <page>0</page>" +
        "    <size>10</size>" +
        "  </pageInfo>" +
        "</allUsersPageRequest>"
    )))
    .andExpect(noFault())
    .andExpect(xpath("//ud:usersResponse/ud:totalElements", NS).evaluatesTo(2))
    .andExpect(xpath("//ud:usersResponse/ud:totalPages", NS).evaluatesTo(1))
    .andExpect(xpath("//ud:usersResponse/ud:number", NS).evaluatesTo(0));
  }

  @Test
  @Sql(FIXTURE)
  void allUsersRequestWithSearchQueryShouldFilterResults() throws Exception {
    mockClient.sendRequest(withPayload(new StringSource(
        "<allUsersRequest xmlns=\"niffler-userdata\">" +
        "  <username>duck</username>" +
        "  <searchQuery>bar</searchQuery>" +
        "</allUsersRequest>"
    )))
    .andExpect(noFault())
    .andExpect(xpath("count(//ud:usersResponse/ud:user)", NS).evaluatesTo(1))
    .andExpect(xpath("//ud:usersResponse/ud:user/ud:username", NS).evaluatesTo("barsik"));
  }
}
