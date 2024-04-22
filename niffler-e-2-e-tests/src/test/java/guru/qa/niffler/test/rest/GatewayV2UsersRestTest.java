package guru.qa.niffler.test.rest;

import guru.qa.niffler.api.GatewayV2ApiClient;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Friends;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.IncomeInvitations;
import guru.qa.niffler.jupiter.annotation.OutcomeInvitations;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.page.RestPage;
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

@Epic("[REST][niffler-gateway]: Пагинация Users")
@DisplayName("[REST][niffler-gateway]: Пагинация Users")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GatewayV2UsersRestTest extends BaseRestTest {

    private static final GatewayV2ApiClient gatewayV2client = new GatewayV2ApiClient();

    @Test
    @AllureId("200003")
    @DisplayName("REST: Список друзей получен в виде Page при передаче параметров page, size c учетом сортировки по полю username ASC")
    @Tag("REST")
    @ApiLogin(user = @GenerateUser(
            friends = @Friends(
                    count = 3
            )
    ))
    void pageableFriendsWithSortAscTest(@User UserJson user,
                                        @Token String bearerToken) throws Exception {
        RestPage<UserJson> firstPage = gatewayV2client.allFriendsPageable(
                bearerToken,
                null,
                0,
                2,
                List.of("username," + Sort.Direction.ASC)
        );
        step("Check that response not null", () ->
                assertNotNull(firstPage)
        );
        step("Check total elements count", () ->
                assertEquals(3L, firstPage.getTotalElements())
        );
        step("Check total pages", () ->
                assertEquals(2, firstPage.getTotalPages())
        );
        final List<UserJson> firstPageContent = firstPage.getContent();

        List<UserJson> userFriends = user.testData().friends();
        Collections.sort(userFriends, Comparator.comparing(UserJson::username));

        step("Check elements size", () ->
                assertEquals(2, firstPageContent.size())
        );
        step("Check first element of first page", () ->
                assertEquals(userFriends.get(0).username(), firstPageContent.get(0).username())
        );
        step("Check second element of first page", () ->
                assertEquals(userFriends.get(1).username(), firstPageContent.get(1).username())
        );
    }

    @Test
    @AllureId("200004")
    @DisplayName("REST: Список друзей получен в виде Page при передаче параметров page, size c учетом сортировки по полю username DESC")
    @Tag("REST")
    @ApiLogin(user = @GenerateUser(
            friends = @Friends(
                    count = 3
            )
    ))
    void pageableFriendsWithSortDescTest(@User UserJson user,
                                         @Token String bearerToken) throws Exception {
        RestPage<UserJson> firstPage = gatewayV2client.allFriendsPageable(
                bearerToken,
                null,
                0,
                2,
                List.of("username," + Sort.Direction.DESC)
        );
        step("Check that response not null", () ->
                assertNotNull(firstPage)
        );
        step("Check total elements count", () ->
                assertEquals(3L, firstPage.getTotalElements())
        );
        step("Check total pages", () ->
                assertEquals(2, firstPage.getTotalPages())
        );
        final List<UserJson> firstPageContent = firstPage.getContent();

        List<UserJson> userFriends = user.testData().friends();
        Collections.sort(userFriends, Comparator.comparing(UserJson::username));
        Collections.reverse(userFriends);

        step("Check elements size", () ->
                assertEquals(2, firstPageContent.size())
        );
        step("Check first element of first page", () ->
                assertEquals(userFriends.get(0).username(), firstPageContent.get(0).username())
        );
        step("Check second element of first page", () ->
                assertEquals(userFriends.get(1).username(), firstPageContent.get(1).username())
        );
    }

    @Test
    @AllureId("200005")
    @DisplayName("REST: Список входящих предложений дружбы получен в виде Page при передаче параметров page, size" +
            " c учетом сортировки по полю username ASC")
    @Tag("REST")
    @ApiLogin(user = @GenerateUser(
            incomeInvitations = @IncomeInvitations(
                    count = 3
            )
    ))
    void pageableIncomeInvitationsWithSortAscTest(@User UserJson user,
                                                  @Token String bearerToken) throws Exception {
        RestPage<UserJson> firstPage = gatewayV2client.incomeInvitationsPageable(
                bearerToken,
                null,
                0,
                2,
                List.of("username," + Sort.Direction.ASC)
        );
        step("Check that response not null", () ->
                assertNotNull(firstPage)
        );
        step("Check total elements count", () ->
                assertEquals(3L, firstPage.getTotalElements())
        );
        step("Check total pages", () ->
                assertEquals(2, firstPage.getTotalPages())
        );
        final List<UserJson> firstPageContent = firstPage.getContent();

        List<UserJson> userInvitations = user.testData().incomeInvitations();
        Collections.sort(userInvitations, Comparator.comparing(UserJson::username));

        step("Check elements size", () ->
                assertEquals(2, firstPageContent.size())
        );
        step("Check first element of first page", () ->
                assertEquals(userInvitations.get(0).username(), firstPageContent.get(0).username())
        );
        step("Check second element of first page", () ->
                assertEquals(userInvitations.get(1).username(), firstPageContent.get(1).username())
        );
    }

    @Test
    @AllureId("200006")
    @DisplayName("REST: Список исходящих предложений дружбы получен в виде Page при передаче параметров page, size" +
            " c учетом сортировки по полю username ASC")
    @Tag("REST")
    @ApiLogin(user = @GenerateUser(
            outcomeInvitations = @OutcomeInvitations(
                    count = 3
            )
    ))
    void pageableOutcomeInvitationsWithSortAscTest(@User UserJson user,
                                                   @Token String bearerToken) throws Exception {
        RestPage<UserJson> firstPage = gatewayV2client.outcomeInvitationsPageable(
                bearerToken,
                null,
                0,
                2,
                List.of("username," + Sort.Direction.ASC)
        );
        step("Check that response not null", () ->
                assertNotNull(firstPage)
        );
        step("Check total elements count", () ->
                assertEquals(3L, firstPage.getTotalElements())
        );
        step("Check total pages", () ->
                assertEquals(2, firstPage.getTotalPages())
        );
        final List<UserJson> firstPageContent = firstPage.getContent();

        List<UserJson> userInvitations = user.testData().outcomeInvitations();
        Collections.sort(userInvitations, Comparator.comparing(UserJson::username));

        step("Check elements size", () ->
                assertEquals(2, firstPageContent.size())
        );
        step("Check first element of first page", () ->
                assertEquals(userInvitations.get(0).username(), firstPageContent.get(0).username())
        );
        step("Check second element of first page", () ->
                assertEquals(userInvitations.get(1).username(), firstPageContent.get(1).username())
        );
    }

    @Test
    @AllureId("200007")
    @DisplayName("REST: Список пользователей получен в виде Page при передаче параметров page, size")
    @Tag("REST")
    @ApiLogin(user = @GenerateUser)
    @Order(1)
    void pageableAllUsersTest(@Token String bearerToken) throws Exception {
        RestPage<UserJson> firstPage = gatewayV2client.allUsersPageable(
                bearerToken,
                null,
                0,
                2,
                List.of("username," + Sort.Direction.ASC)
        );
        step("Check that response not null", () ->
                assertNotNull(firstPage)
        );
        step("Check that total elements count > 0", () ->
                assertTrue(firstPage.getTotalElements() > 0)
        );
        step("Check total pages count > -", () ->
                assertTrue(firstPage.getTotalPages() > 0)
        );
    }
}
