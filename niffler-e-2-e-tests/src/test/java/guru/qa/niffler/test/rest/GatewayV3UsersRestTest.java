package guru.qa.niffler.test.rest;

import guru.qa.niffler.api.GatewayV3ApiClient;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Friends;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.IncomeInvitations;
import guru.qa.niffler.jupiter.annotation.OutcomeInvitations;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.page.PagedModelJson;
import guru.qa.niffler.model.rest.FriendshipStatus;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("[REST][niffler-gateway]: Пагинация Users V2")
@DisplayName("[REST][niffler-gateway]: Пагинация Users V2")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GatewayV3UsersRestTest extends BaseRestTest {

  private static final GatewayV3ApiClient gatewayV3client = new GatewayV3ApiClient();

  @Test
  @AllureId("200027")
  @DisplayName("REST: Список друзей получен в виде Page при передаче параметров page, size " +
      "c учетом сортировки по статусу дружбы (приглашения первые) и по полю username ASC")
  @Tag("REST")
  @ApiLogin(user = @GenerateUser(
      friends = @Friends(count = 3),
      incomeInvitations = @IncomeInvitations(count = 3)
  ))
  void pageableFriendsWithSortAscTest(@User UserJson user,
                                      @Token String bearerToken) throws Exception {
    List<UserJson> userFriends = user.testData().friends();
    List<UserJson> userInvitation = user.testData().incomeInvitations();

    PagedModelJson<UserJson> firstPage = gatewayV3client.allFriendsPageable(
        bearerToken,
        null,
        0,
        5,
        List.of("username," + Sort.Direction.ASC)
    );
    step("Check that response not null", () ->
        assertNotNull(firstPage)
    );
    step("Check total elements count", () ->
        assertEquals(6L, firstPage.getMetadata().totalElements())
    );
    step("Check total pages", () ->
        assertEquals(2, firstPage.getMetadata().totalPages())
    );
    final List<UserJson> firstPageContent = firstPage.getContent();

    userFriends.sort(Comparator.comparing(UserJson::username));
    userInvitation.sort(Comparator.comparing(UserJson::username));

    step("Check elements size", () ->
        assertEquals(5, firstPageContent.size())
    );
    step("Check 0 element with INVITE_RECEIVED state on first page", () ->
        assertEquals(userInvitation.getFirst().username(), firstPageContent.getFirst().username())
    );
    step("Check 1 element with INVITE_RECEIVED state on first page", () ->
        assertEquals(userInvitation.get(1).username(), firstPageContent.get(1).username())
    );
    step("Check 2 element with INVITE_RECEIVED state on first page", () ->
        assertEquals(userInvitation.get(2).username(), firstPageContent.get(2).username())
    );
    step("Check first element with FRIEND state on first page", () ->
        assertEquals(userFriends.getFirst().username(), firstPageContent.get(3).username())
    );
  }

  @Test
  @AllureId("200028")
  @DisplayName("REST: Список друзей получен в виде Page при передаче параметров page, size " +
      "c учетом сортировки по статусу дружбы (приглашения первые) и по полю username DESC")
  @Tag("REST")
  @ApiLogin(user = @GenerateUser(
      friends = @Friends(
          count = 3
      ),
      incomeInvitations = @IncomeInvitations(count = 1)
  ))
  void pageableFriendsWithSortDescTest(@User UserJson user,
                                       @Token String bearerToken) throws Exception {
    PagedModelJson<UserJson> firstPage = gatewayV3client.allFriendsPageable(
        bearerToken,
        null,
        0,
        3,
        List.of("username," + Sort.Direction.DESC)
    );
    step("Check that response not null", () ->
        assertNotNull(firstPage)
    );
    step("Check total elements count", () ->
        assertEquals(4L, firstPage.getMetadata().totalElements())
    );
    step("Check total pages", () ->
        assertEquals(2, firstPage.getMetadata().totalPages())
    );
    final List<UserJson> firstPageContent = firstPage.getContent();

    List<UserJson> userFriends = user.testData().friends();
    List<UserJson> userInvitation = user.testData().incomeInvitations();
    userFriends.sort(Comparator.comparing(UserJson::username));
    userInvitation.sort(Comparator.comparing(UserJson::username));
    Collections.reverse(userFriends);
    Collections.reverse(userInvitation);

    step("Check elements size", () ->
        assertEquals(3, firstPageContent.size())
    );
    step("Check first element (invitation) of first page", () ->
        assertEquals(userInvitation.getFirst().username(), firstPageContent.getFirst().username())
    );
    step("Check first element of first page", () ->
        assertEquals(userFriends.getFirst().username(), firstPageContent.get(1).username())
    );
    step("Check second element of first page", () ->
        assertEquals(userFriends.get(1).username(), firstPageContent.get(2).username())
    );
  }

  @Test
  @AllureId("200029")
  @DisplayName("REST: Список пользователей получен в виде Page при передаче параметров page, size " +
      "c учетом сортировки по статусу дружбы (иcходящие приглашения первые)")
  @Tag("REST")
  @ApiLogin(user = @GenerateUser(outcomeInvitations = @OutcomeInvitations(count = 1)))
  @Order(1)
  void pageableAllUsersTest(@User UserJson user,
                            @Token String bearerToken) throws Exception {
    PagedModelJson<UserJson> firstPage = gatewayV3client.allUsersPageable(
        bearerToken,
        null,
        0,
        10,
        null
    );
    step("Check that response not null", () ->
        assertNotNull(firstPage)
    );
    step("Check that total elements count > 0", () ->
        assertTrue(firstPage.getMetadata().totalElements() > 0)
    );
    step("Check total pages count > -", () ->
        assertTrue(firstPage.getMetadata().totalPages() > 0)
    );
    step("Check that first element is outcome invitation", () ->
        assertEquals(FriendshipStatus.INVITE_SENT, firstPage.getContent().getFirst().friendshipStatus())
    );
  }
}
