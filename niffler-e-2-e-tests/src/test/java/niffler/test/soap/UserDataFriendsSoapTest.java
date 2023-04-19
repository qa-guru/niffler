package niffler.test.soap;

import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import niffler.jupiter.annotation.Friends;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.OutcomeInvitations;
import niffler.jupiter.annotation.User;
import niffler.model.rest.UserJson;
import niffler.model.soap.FriendState;
import niffler.model.soap.FriendsRequest;
import niffler.model.soap.FriendsResponse;
import niffler.ws.NifflerUserdataWsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static io.qameta.allure.Allure.step;
import static niffler.jupiter.extension.CreateUserExtension.Selector.METHOD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("[SOAP][niffler-userdata]: Друзья")
@DisplayName("[SOAP][niffler-userdata]: Друзья")
public class UserDataFriendsSoapTest extends BaseSoapTest {

    private static final String ID_REGEXP = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    private final NifflerUserdataWsService nus = new NifflerUserdataWsService();

    @Test
    @DisplayName("SOAP: Для пользователя должен возвращаться список друзей из niffler-userdata" +
            " при передаче includePending = false")
    @AllureId("100004")
    @Tag("SOAP")
    @GenerateUser(
            friends = @Friends(count = 1),
            outcomeInvitations = @OutcomeInvitations(count = 1)
    )
    void getAllFriendsListWithoutInvitationTest(@User(selector = METHOD) UserJson user) throws Exception {
        FriendsRequest fr = new FriendsRequest();
        fr.setUsername(user.getUsername());
        fr.setIncludePending(false);

        final FriendsResponse friendsResponse = nus.friendsRequest(fr);

        List<niffler.model.soap.User> friends = friendsResponse.getUser();

        step("Check that response contains expected users", () ->
                assertEquals(1, friends.size())
        );

        Optional<niffler.model.soap.User> friend = friends.stream()
                .filter(u -> u.getFriendState() == FriendState.FRIEND)
                .findFirst();

        Optional<niffler.model.soap.User> invitation = friends.stream()
                .filter(u -> u.getFriendState() == FriendState.INVITE_SENT)
                .findFirst();

        step("Check friend in response", () -> {
            assertTrue(friend.isPresent());
            assertEquals(user.getFriendsJsons().get(0).getUsername(), friend.get().getUsername());
        });

        step("Check that no invitation  present in response", () -> {
            assertFalse(invitation.isPresent());
        });
    }

    @Test
    @DisplayName("SOAP: Для пользователя должен возвращаться список друзей и инвайтов из niffler-userdata" +
            " при передаче includePending = true")
    @AllureId("100005")
    @Tag("SOAP")
    @GenerateUser(
            friends = @Friends(count = 1),
            outcomeInvitations = @OutcomeInvitations(count = 1)
    )
    void getAllFriendsListWithInvitationTest(@User(selector = METHOD) UserJson user) throws Exception {
        FriendsRequest fr = new FriendsRequest();
        fr.setUsername(user.getUsername());
        fr.setIncludePending(true);

        final FriendsResponse friendsResponse = nus.friendsRequest(fr);

        List<niffler.model.soap.User> friends = friendsResponse.getUser();

        step("Check that response contains expected users", () ->
                assertEquals(2, friends.size())
        );

        Optional<niffler.model.soap.User> friend = friends.stream()
                .filter(u -> u.getFriendState() == FriendState.FRIEND)
                .findFirst();

        Optional<niffler.model.soap.User> invitation = friends.stream()
                .filter(u -> u.getFriendState() == FriendState.INVITE_SENT)
                .findFirst();

        step("Check friend in response", () -> {
            assertTrue(friend.isPresent());
            assertEquals(user.getFriendsJsons().get(0).getUsername(), friend.get().getUsername());
        });

        step("Check invitation in response", () -> {
            assertTrue(invitation.isPresent());
            assertEquals(user.getInvitationsJsons().get(0).getUsername(), invitation.get().getUsername());
        });
    }
}
