package guru.qa.niffler.test.rest;

import guru.qa.niffler.api.UserdataApiClient;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Friends;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.OutcomeInvitations;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.FriendState;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("[REST][niffler-gateway]: Друзья")
@DisplayName("[REST][niffler-gateway]: Друзья")
public class GatewayFriendsRestTest extends BaseRestTest {

    private static final UserdataApiClient userdataApiClient = new UserdataApiClient();

    @Test
    @DisplayName("REST: Для пользователя должен возвращаться список друзей из niffler-userdata")
    @AllureId("200012")
    @Tag("REST")
    @ApiLogin(
            user = @GenerateUser(
                    friends = @Friends(count = 1),
                    outcomeInvitations = @OutcomeInvitations(count = 1)
            )
    )
    void getAllFriendsListTest(@User UserJson user,
                               @Token String bearerToken) throws Exception {
        UserJson testFriend = user.testData().friends().get(0);

        final List<UserJson> friends = gatewayApiClient.allFriends(bearerToken, null);
        step("Check that response not null", () ->
                assertNotNull(friends)
        );
        step("Check that response contains expected users", () ->
                assertEquals(1, friends.size())
        );

        final var foundedFriend = friends.get(0);

        step("Check friend in response", () -> {
            assertSame(FriendState.FRIEND, foundedFriend.friendState());
            assertEquals(testFriend.id(), foundedFriend.id());
            assertEquals(testFriend.username(), foundedFriend.username());
        });
    }

    @Test
    @DisplayName("REST: Для пользователя должен возвращаться список друзей из niffler-userdata " +
            "с фильтраций по username, если передан searchQuery")
    @AllureId("200013")
    @Tag("REST")
    @ApiLogin(
            user = @GenerateUser(
                    friends = @Friends(count = 2),
                    outcomeInvitations = @OutcomeInvitations(count = 1)
            )
    )
    void getFilteredFriendsListTest(@User UserJson user,
                                    @Token String bearerToken) throws Exception {
        UserJson testFriend = user.testData().friends().get(0);

        final List<UserJson> friends = gatewayApiClient.allFriends(bearerToken, testFriend.username());
        step("Check that response not null", () ->
                assertNotNull(friends)
        );
        step("Check that response contains expected users", () ->
                assertEquals(1, friends.size())
        );

        final var foundedFriend = friends.get(0);

        step("Check friend in response", () -> {
            assertSame(FriendState.FRIEND, foundedFriend.friendState());
            assertEquals(testFriend.id(), foundedFriend.id());
            assertEquals(testFriend.username(), foundedFriend.username());
        });
    }

    @Test
    @DisplayName("REST: Удаление друга")
    @AllureId("200014")
    @Tag("REST")
    @ApiLogin(
            user = @GenerateUser(
                    friends = @Friends(count = 1)
            )
    )
    void removeFriendTest(@User UserJson user,
                          @Token String bearerToken) throws Exception {
        final String friendUsername = user.testData().friends().get(0).username();
        gatewayApiClient.removeFriend(bearerToken, friendUsername);

        step("Check that no friends present in GET /friends request for both users", () ->
                Assertions.assertAll(
                        () -> assertTrue(
                                gatewayApiClient.allFriends(bearerToken, null).isEmpty(),
                                "Current user should not have friend after removing"
                        ),
                        () -> assertTrue(
                                userdataApiClient.friends(friendUsername, null).isEmpty(),
                                "Target friend should not have friend after removing"
                        )
                )
        );
    }
}
