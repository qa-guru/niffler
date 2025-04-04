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
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static guru.qa.niffler.utils.DataUtils.randomPassword;
import static guru.qa.niffler.utils.DataUtils.randomUsername;
import static java.util.Objects.requireNonNull;

@ParametersAreNonnullByDefault
public class RestCreateUserExtension extends AbstractCreateUserExtension {

  private static final AuthApiClient authClient = new AuthApiClient();
  private static final UserdataApiClient userdataClient = new UserdataApiClient();
  private static final SpendApiClient spendClient = new SpendApiClient();

  @Step("Create user for test (REST)")
  @Override
  @Nonnull
  protected UserJson createUser(String username,
                                String password) throws Exception {
    try {
      authClient.register(username, password);
    } finally {
      ThreadLocalCookieStore.INSTANCE.removeAll();
    }
    UserJson currentUser = waitWhileUserToBeConsumed(username, 10000L);
    return currentUser.addTestData(new TestData(password));
  }

  @Step("Create income invitations for test user (REST)")
  @Override
  protected void createIncomeInvitationsIfPresent(IncomeInvitations incomeInvitations,
                                                  UserJson createdUser) throws Exception {
    if (incomeInvitations.handleAnnotation() && incomeInvitations.count() > 0) {
      for (int i = 0; i < incomeInvitations.count(); i++) {
        UserJson invitation = createUser(randomUsername(), randomPassword());
        userdataClient.sendInvitation(invitation.username(), createdUser.username());
        createdUser.testData().incomeInvitations().add(invitation);
      }
    }
  }

  @Step("Create outcome invitations for test user (REST)")
  @Override
  protected void createOutcomeInvitationsIfPresent(OutcomeInvitations outcomeInvitations,
                                                   UserJson createdUser) throws Exception {
    if (outcomeInvitations.handleAnnotation() && outcomeInvitations.count() > 0) {
      for (int i = 0; i < outcomeInvitations.count(); i++) {
        UserJson friend = createUser(randomUsername(), randomPassword());
        userdataClient.sendInvitation(createdUser.username(), friend.username());
        createdUser.testData().outcomeInvitations().add(friend);
      }
    }
  }

  @Step("Create friends for test user (REST)")
  @Override
  protected void createFriendsIfPresent(Friends friends,
                                        UserJson createdUser) throws Exception {
    if (friends.handleAnnotation() && friends.count() > 0) {
      for (int i = 0; i < friends.count(); i++) {
        UserJson friend = createUser(randomUsername(), randomPassword());
        createCategoriesIfPresent(friends.categories(), friend);
        userdataClient.sendInvitation(createdUser.username(), friend.username());
        userdataClient.acceptInvitation(friend.username(), createdUser.username());
        createdUser.testData().friends().add(friend);
      }
    }
  }

  @Step("Create spends for test user (REST)")
  @Override
  protected void createSpendsIfPresent(GenerateSpend[] spends, UserJson createdUser) throws Exception {
    if (ArrayUtils.isNotEmpty(spends)) {
      List<CategoryJson> userCategories = spendClient.allCategories(createdUser.username());
      for (GenerateSpend spend : spends) {
        Optional<CategoryJson> existingCategory = userCategories.stream()
            .filter(cat -> cat.name().equals(spend.category()))
            .findFirst();

        SpendJson sj = new SpendJson(
            null,
            DateUtils.addDaysToDate(new Date(), Calendar.DAY_OF_WEEK, spend.addDaysToSpendDate()),
            spend.amount(),
            spend.currency(),
            existingCategory.orElseGet(() -> new CategoryJson(
                null,
                spend.category(),
                createdUser.username(),
                false
            )),
            spend.name(),
            createdUser.username()
        );
        createdUser.testData().spends().add(spendClient.createSpend(sj));
      }
    }
  }

  @Step("Create categories for test user (REST)")
  @Override
  protected void createCategoriesIfPresent(GenerateCategory[] categories, UserJson createdUser) throws Exception {
    if (ArrayUtils.isNotEmpty(categories)) {
      for (GenerateCategory category : categories) {
        CategoryJson cj = new CategoryJson(null, category.name(), createdUser.username(), category.archived());
        CategoryJson created = requireNonNull(spendClient.createCategory(cj));
        if (category.archived()) {
          created = spendClient.updateCategory(
              new CategoryJson(
                  created.id(),
                  created.name(),
                  created.username(),
                  true
              )
          );
        }
        createdUser.testData().categories().add(created);
      }
    }
  }

  private UserJson waitWhileUserToBeConsumed(String username, long maxWaitTime) throws Exception {
    Stopwatch sw = Stopwatch.createStarted();
    while (sw.elapsed(TimeUnit.MILLISECONDS) < maxWaitTime) {
      UserJson userJson = userdataClient.getCurrentUser(username);
      if (userJson != null && userJson.id() != null) {
        return userJson;
      } else {
        Thread.sleep(100);
      }
    }
    throw new IllegalStateException("Can`t obtain user from niffler-userdata");
  }
}
