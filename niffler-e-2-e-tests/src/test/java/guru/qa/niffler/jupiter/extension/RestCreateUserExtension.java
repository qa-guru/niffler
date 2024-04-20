package guru.qa.niffler.jupiter.extension;

import com.google.common.base.Stopwatch;
import guru.qa.niffler.api.AuthApiClient;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.api.UserdataApiClient;
import guru.qa.niffler.api.service.ThreadLocalCookieStore;
import guru.qa.niffler.jupiter.annotation.Friends;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.IncomeInvitations;
import guru.qa.niffler.jupiter.annotation.OutcomeInvitations;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.TestData;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.utils.DateUtils;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static guru.qa.niffler.utils.DataUtils.generateRandomPassword;
import static guru.qa.niffler.utils.DataUtils.generateRandomUsername;

public class RestCreateUserExtension extends AbstractCreateUserExtension {

    private static final AuthApiClient authClient = new AuthApiClient();
    private static final UserdataApiClient userdataClient = new UserdataApiClient();
    private static final SpendApiClient spendClient = new SpendApiClient();

    @Step("Create user for test (REST)")
    @Override
    @Nonnull
    protected UserJson createUser(@Nonnull String username,
                                  @Nonnull String password) throws Exception {
        authClient.register(username, password);
        ThreadLocalCookieStore.INSTANCE.removeAll();
        UserJson currentUser = waitWhileUserToBeConsumed(username, 10000L);
        return currentUser.addTestData(new TestData(
                password,
                new ArrayList<>(),
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
                userdataClient.sendInvitation(invitation.username(), createdUser.username());
                createdUser.testData().incomeInvitations().add(invitation);
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
                userdataClient.sendInvitation(createdUser.username(), friend.username());
                createdUser.testData().outcomeInvitations().add(friend);
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
                userdataClient.sendInvitation(createdUser.username(), friend.username());
                userdataClient.acceptInvitation(friend.username(), createdUser.username());
                createdUser.testData().friends().add(friend);
            }
        }
    }

    @Step("Create spends for test user (REST)")
    @Override
    protected void createSpendsIfPresent(@Nullable GenerateSpend[] spends, @Nonnull UserJson createdUser) throws Exception {
        if (spends != null) {
            for (GenerateSpend spend : spends) {
                SpendJson sj = new SpendJson(
                        null,
                        DateUtils.addDaysToDate(new Date(), Calendar.DAY_OF_WEEK, spend.addDaysToSpendDate()),
                        spend.amount(),
                        spend.currency(),
                        spend.spendCategory(),
                        spend.spendName(),
                        createdUser.username()
                );
                createdUser.testData().spends().add(spendClient.createSpend(sj));
            }
        }
    }

    @Step("Create categories for test user (REST)")
    @Override
    protected void createCategoriesIfPresent(@Nullable GenerateCategory[] categories, @Nonnull UserJson createdUser) throws Exception {
        if (categories != null) {
            for (GenerateCategory category : categories) {
                CategoryJson cj = new CategoryJson(null, category.value(), createdUser.username());
                createdUser.testData().categories().add(spendClient.createCategory(cj));
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
