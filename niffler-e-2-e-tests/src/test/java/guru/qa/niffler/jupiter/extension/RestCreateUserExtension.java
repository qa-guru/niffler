package guru.qa.niffler.jupiter.extension;

import com.google.common.base.Stopwatch;
import guru.qa.niffler.api.AuthRestClient;
import guru.qa.niffler.api.UserdataRestClient;
import guru.qa.niffler.jupiter.annotation.Friends;
import guru.qa.niffler.jupiter.annotation.IncomeInvitations;
import guru.qa.niffler.jupiter.annotation.OutcomeInvitations;
import guru.qa.niffler.model.rest.FriendJson;
import guru.qa.niffler.model.rest.TestData;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static guru.qa.niffler.utils.DataUtils.generateRandomPassword;
import static guru.qa.niffler.utils.DataUtils.generateRandomUsername;

public class RestCreateUserExtension extends AbstractCreateUserExtension {

    private final AuthRestClient authClient = new AuthRestClient();
    private final UserdataRestClient userdataClient = new UserdataRestClient();

    @Step("Create user for test (REST)")
    @Override
    @Nonnull
    protected UserJson createUser(@Nonnull String username,
                                  @Nonnull String password) throws Exception {
        authClient.register(username, password);
        UserJson currentUser = waitWhileUserToBeConsumed(username, 10000L);
        return currentUser.addTestData(new TestData(
                password,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        ));
    }

    @Step("Create income invitations for test user (REST)")
    @Override
    protected void createIncomeInvitationsIfPresent(@Nonnull IncomeInvitations incomeInvitations,
                                                    @Nonnull UserJson createdUser) throws Exception {
        if (incomeInvitations.handleAnnotation() && incomeInvitations.count() > 0) {
            for (int i = 0; i < incomeInvitations.count(); i++) {
                UserJson invitation = createUser(generateRandomUsername(), generateRandomPassword());
                FriendJson addFriend = new FriendJson(createdUser.username());
                userdataClient.addFriend(invitation.username(), addFriend);
                createdUser.testData().invitationsJsons().add(invitation);
            }
        }
    }

    @Step("Create outcome invitations for test user (REST)")
    @Override
    protected void createOutcomeInvitationsIfPresent(@Nonnull OutcomeInvitations outcomeInvitations,
                                                     @Nonnull UserJson createdUser) throws Exception {
        if (outcomeInvitations.handleAnnotation() && outcomeInvitations.count() > 0) {
            for (int i = 0; i < outcomeInvitations.count(); i++) {
                UserJson friend = createUser(generateRandomUsername(), generateRandomPassword());
                FriendJson addFriend = new FriendJson(friend.username());
                userdataClient.addFriend(createdUser.username(), addFriend);
                createdUser.testData().invitationsJsons().add(friend);
            }
        }
    }

    @Step("Create friends for test user (REST)")
    @Override
    protected void createFriendsIfPresent(@Nonnull Friends friends,
                                          @Nonnull UserJson createdUser) throws Exception {
        if (friends.handleAnnotation() && friends.count() > 0) {
            for (int i = 0; i < friends.count(); i++) {
                UserJson friend = createUser(generateRandomUsername(), generateRandomPassword());
                FriendJson addFriend = new FriendJson(friend.username());
                FriendJson invitation = new FriendJson(createdUser.username());
                userdataClient.addFriend(createdUser.username(), addFriend);
                userdataClient.acceptInvitation(friend.username(), invitation);
                createdUser.testData().friendsJsons().add(friend);
            }
        }
    }

    private UserJson waitWhileUserToBeConsumed(@Nonnull String username, long maxWaitTime) throws Exception {
        Stopwatch sw = Stopwatch.createStarted();
        while (sw.elapsed(TimeUnit.MILLISECONDS) < maxWaitTime) {
            UserJson userJson = userdataClient.getCurrentUser(username);
            if (userJson != null) {
                return userJson;
            } else {
                Thread.sleep(100);
            }
        }
        throw new IllegalStateException("Can`t obtain user from niffler-userdata");
    }
}
