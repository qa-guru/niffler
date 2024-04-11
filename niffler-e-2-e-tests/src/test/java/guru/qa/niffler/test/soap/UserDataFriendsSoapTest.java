package guru.qa.niffler.test.soap;

import guru.qa.niffler.jupiter.annotation.Friends;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.OutcomeInvitations;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.userdata.wsdl.FriendState;
import guru.qa.niffler.userdata.wsdl.FriendsRequest;
import guru.qa.niffler.userdata.wsdl.RemoveFriendRequest;
import guru.qa.niffler.userdata.wsdl.UsersResponse;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

import static guru.qa.niffler.jupiter.annotation.User.Selector.METHOD;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("[SOAP][niffler-userdata]: Друзья")
@DisplayName("[SOAP][niffler-userdata]: Друзья")
public class UserDataFriendsSoapTest extends BaseSoapTest {

    @Test
    @DisplayName("SOAP: Для пользователя должен возвращаться список друзей из niffler-userdata")
    @AllureId("100004")
    @Tag("SOAP")
    @GenerateUser(
            friends = @Friends(count = 1),
            outcomeInvitations = @OutcomeInvitations(count = 1)
    )
    void getAllFriendsListTest(@User(selector = METHOD) UserJson user) throws Exception {
        UserJson testFriend = user.testData().friends().get(0);
        FriendsRequest fr = friendsRequest(user.username(), null);
        final UsersResponse usersResponse = wsClient.friendsRequest(fr);

        step("Check that response not null", () ->
                assertNotNull(usersResponse)
        );
        step("Check response list size", () ->
                assertEquals(1, usersResponse.getUser().size())
        );

        final var foundedFriend = usersResponse.getUser().get(0);

        step("Check friend in response", () -> {
            assertSame(FriendState.FRIEND, foundedFriend.getFriendState());
            assertEquals(testFriend.id(), UUID.fromString(foundedFriend.getId()));
            assertEquals(testFriend.username(), foundedFriend.getUsername());
        });
    }

    @Test
    @DisplayName("SOAP: Для пользователя должен возвращаться список друзей из niffler-userdata " +
            "с фильтраций по username, если передан searchQuery")
    @AllureId("100005")
    @Tag("SOAP")
    @GenerateUser(
            friends = @Friends(count = 2),
            outcomeInvitations = @OutcomeInvitations(count = 1)
    )
    void getFiltersFriendsListTest(@User(selector = METHOD) UserJson user) throws Exception {
        UserJson testFriend = user.testData().friends().get(0);
        FriendsRequest fr = friendsRequest(user.username(), testFriend.username());
        final UsersResponse usersResponse = wsClient.friendsRequest(fr);

        step("Check that response not null", () ->
                assertNotNull(usersResponse)
        );
        step("Check response list size", () ->
                assertEquals(1, usersResponse.getUser().size())
        );

        final var foundedFriend = usersResponse.getUser().get(0);

        step("Check friend in response", () -> {
            assertSame(FriendState.FRIEND, foundedFriend.getFriendState());
            assertEquals(testFriend.id(), UUID.fromString(foundedFriend.getId()));
            assertEquals(testFriend.username(), foundedFriend.getUsername());
        });
    }

    @Test
    @DisplayName("SOAP: Удаление друга")
    @AllureId("10006")
    @Tag("SOAP")
    @GenerateUser(
            friends = @Friends(count = 1)
    )
    void removeFriendTest(@User(selector = METHOD) UserJson user) throws Exception {
        final String currentUsername = user.username();
        final String friendUsername = user.testData().friends().get(0).username();

        RemoveFriendRequest rfr = removeFriendRequest(currentUsername, friendUsername);

        wsClient.removeFriendRequest(rfr);

        step("Check that no friends present in GET /friends request for both users", () ->
                Assertions.assertAll(
                        () -> assertTrue(
                                wsClient.friendsRequest(friendsRequest(currentUsername, null))
                                        .getUser()
                                        .isEmpty(),
                                "Current user should not have friend after removing"
                        ),
                        () -> assertTrue(
                                wsClient.friendsRequest(friendsRequest(friendUsername, null))
                                        .getUser()
                                        .isEmpty(),
                                "Target friend should not have friend after removing"
                        )
                )
        );
    }

    private @Nonnull FriendsRequest friendsRequest(@Nonnull String username, @Nullable String searchQuery) {
        FriendsRequest fr = new FriendsRequest();
        fr.setUsername(username);
        fr.setSearchQuery(searchQuery);
        return fr;
    }

    private @Nonnull RemoveFriendRequest removeFriendRequest(@Nonnull String username, @Nonnull String friendUsername) {
        RemoveFriendRequest rfr = new RemoveFriendRequest();
        rfr.setUsername(username);
        rfr.setFriendToBeRemoved(friendUsername);
        return rfr;
    }
}
