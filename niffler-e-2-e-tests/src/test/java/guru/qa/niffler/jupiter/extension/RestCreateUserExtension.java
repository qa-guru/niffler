package guru.qa.niffler.jupiter.extension;

import com.google.common.base.Stopwatch;
import guru.qa.niffler.api.NifflerAuthClient;
import guru.qa.niffler.api.NifflerUserdataClient;
import guru.qa.niffler.jupiter.annotation.Friends;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.IncomeInvitations;
import guru.qa.niffler.jupiter.annotation.OutcomeInvitations;
import guru.qa.niffler.model.rest.FriendJson;
import guru.qa.niffler.model.rest.TestData;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.Step;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static guru.qa.niffler.utils.DataUtils.generateRandomPassword;
import static guru.qa.niffler.utils.DataUtils.generateRandomUsername;

public class RestCreateUserExtension extends AbstractCreateUserExtension {

    private final NifflerAuthClient authClient = new NifflerAuthClient();
    private final NifflerUserdataClient userdataClient = new NifflerUserdataClient();

    @Step("Create user for test (REST)")
    @Override
    protected UserJson createUser(String username, String password) throws Exception {
        Response<Void> res = authClient.register(username, password);
        if (res.code() != 201) {
            throw new RuntimeException("User is not registered");
        }
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
    protected void createIncomeInvitationsIfPresent(GenerateUser generateUser, UserJson createdUser) throws Exception {
        IncomeInvitations invitations = generateUser.incomeInvitations();
        if (invitations.handleAnnotation() && invitations.count() > 0) {
            for (int i = 0; i < invitations.count(); i++) {
                UserJson invitation = createUser(generateRandomUsername(), generateRandomPassword());
                FriendJson addFriend = new FriendJson(createdUser.username());
                userdataClient.addFriend(invitation.username(), addFriend);
                createdUser.testData().invitationsJsons().add(invitation);
            }
        }
    }

    @Step("Create outcome invitations for test user (REST)")
    @Override
    protected void createOutcomeInvitationsIfPresent(GenerateUser generateUser, UserJson createdUser) throws Exception {
        OutcomeInvitations invitations = generateUser.outcomeInvitations();
        if (invitations.handleAnnotation() && invitations.count() > 0) {
            for (int i = 0; i < invitations.count(); i++) {
                UserJson friend = createUser(generateRandomUsername(), generateRandomPassword());
                FriendJson addFriend = new FriendJson(friend.username());
                userdataClient.addFriend(createdUser.username(), addFriend);
                createdUser.testData().invitationsJsons().add(friend);
            }
        }
    }

    @Step("Create friends for test user (REST)")
    @Override
    protected void createFriendsIfPresent(GenerateUser generateUser, UserJson createdUser) throws Exception {
        Friends friends = generateUser.friends();
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

    private UserJson waitWhileUserToBeConsumed(String username, long maxWaitTime) throws Exception {
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
