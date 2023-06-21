package guru.qa.niffler.jupiter.extension;

import com.google.common.base.Stopwatch;
import guru.qa.niffler.api.NifflerAuthClient;
import guru.qa.niffler.api.NifflerSpendClient;
import guru.qa.niffler.api.NifflerUserdataClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Friends;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.GenerateUsers;
import guru.qa.niffler.jupiter.annotation.IncomeInvitations;
import guru.qa.niffler.jupiter.annotation.OutcomeInvitations;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.FriendJson;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.utils.DateUtils;
import io.qameta.allure.AllureId;
import io.qameta.allure.Step;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import retrofit2.Response;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static guru.qa.niffler.utils.DataUtils.generateRandomPassword;
import static guru.qa.niffler.utils.DataUtils.generateRandomUsername;

public class CreateUserExtension implements BeforeEachCallback, ParameterResolver {

    private final NifflerAuthClient authClient = new NifflerAuthClient();
    private final NifflerUserdataClient userdataClient = new NifflerUserdataClient();
    private final NifflerSpendClient spendClient = new NifflerSpendClient();
    protected static final Config CFG = Config.getConfig();

    public static final ExtensionContext.Namespace
            ON_METHOD_USERS_NAMESPACE = ExtensionContext.Namespace.create(CreateUserExtension.class, Selector.METHOD),
            API_LOGIN_USERS_NAMESPACE = ExtensionContext.Namespace.create(CreateUserExtension.class, Selector.NESTED);

    @Step("Create user for test")
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        final String testId = getTestId(context);
        Map<Selector, List<GenerateUser>> userAnnotations = extractGenerateUserAnnotations(context);
        for (Map.Entry<Selector, List<GenerateUser>> entry : userAnnotations.entrySet()) {
            UserJson[] resultCollector = new UserJson[entry.getValue().size()];
            List<GenerateUser> value = entry.getValue();
            for (int i = 0; i < value.size(); i++) {
                GenerateUser generateUser = value.get(i);
                String username = generateUser.username();
                String password = generateUser.password();
                if ("".equals(username)) {
                    username = generateRandomUsername();
                }
                if ("".equals(password)) {
                    password = generateRandomPassword();
                }
                UserJson createdUser = apiRegister(username, password);

                createCategoriesIfPresent(generateUser, createdUser);
                createSpendsIfPresent(generateUser, createdUser);
                createFriendsIfPresent(generateUser, createdUser);
                createIncomeInvitationsIfPresent(generateUser, createdUser);
                createOutcomeInvitationsIfPresent(generateUser, createdUser);
                resultCollector[i] = createdUser;
            }
            Object storedResult = resultCollector.length == 1 ? resultCollector[0] : resultCollector;
            context.getStore(entry.getKey().getNamespace()).put(testId, storedResult);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> parameterType = parameterContext.getParameter().getType();
        return (parameterType.isAssignableFrom(UserJson.class) || parameterType.isAssignableFrom(UserJson[].class))
                && parameterContext.getParameter().isAnnotationPresent(User.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final String testId = getTestId(extensionContext);
        User annotation = parameterContext.getParameter().getAnnotation(User.class);
        return extensionContext.getStore(annotation.selector().getNamespace()).get(testId);
    }

    private void createIncomeInvitationsIfPresent(GenerateUser generateUser, UserJson createdUser) throws Exception {
        IncomeInvitations invitations = generateUser.incomeInvitations();
        if (invitations.handleAnnotation() && invitations.count() > 0) {
            for (int i = 0; i < invitations.count(); i++) {
                UserJson invitation = apiRegister(generateRandomUsername(), generateRandomPassword());
                FriendJson addFriend = new FriendJson();
                addFriend.setUsername(createdUser.getUsername());
                userdataClient.addFriend(invitation.getUsername(), addFriend);
                createdUser.getInvitationsJsons().add(invitation);
            }
        }
    }

    private void createOutcomeInvitationsIfPresent(GenerateUser generateUser, UserJson createdUser) throws Exception {
        OutcomeInvitations invitations = generateUser.outcomeInvitations();
        if (invitations.handleAnnotation() && invitations.count() > 0) {
            for (int i = 0; i < invitations.count(); i++) {
                UserJson friend = apiRegister(generateRandomUsername(), generateRandomPassword());
                FriendJson addFriend = new FriendJson();
                addFriend.setUsername(friend.getUsername());
                userdataClient.addFriend(createdUser.getUsername(), addFriend);
                createdUser.getInvitationsJsons().add(friend);
            }
        }
    }

    private void createFriendsIfPresent(GenerateUser generateUser, UserJson createdUser) throws Exception {
        Friends friends = generateUser.friends();
        if (friends.handleAnnotation() && friends.count() > 0) {
            for (int i = 0; i < friends.count(); i++) {
                UserJson friend = apiRegister(generateRandomUsername(), generateRandomPassword());
                FriendJson addFriend = new FriendJson();
                FriendJson invitation = new FriendJson();
                addFriend.setUsername(friend.getUsername());
                invitation.setUsername(createdUser.getUsername());
                userdataClient.addFriend(createdUser.getUsername(), addFriend);
                userdataClient.acceptInvitation(friend.getUsername(), invitation);
                createdUser.getFriendsJsons().add(friend);
            }
        }
    }

    private void createSpendsIfPresent(GenerateUser generateUser, UserJson createdUser) throws Exception {
        GenerateSpend[] spends = generateUser.spends();
        if (spends != null) {
            for (GenerateSpend spend : spends) {
                SpendJson sj = new SpendJson();
                sj.setUsername(createdUser.getUsername());
                sj.setCategory(spend.spendCategory());
                sj.setAmount(spend.amount());
                sj.setCurrency(spend.currency());
                sj.setDescription(spend.spendName());
                sj.setSpendDate(DateUtils.addDaysToDate(new Date(), Calendar.DAY_OF_WEEK, spend.addDaysToSpendDate()));
                createdUser.getSpendJsons().add(spendClient.createSpend(sj));
            }
        }
    }

    private void createCategoriesIfPresent(GenerateUser generateUser, UserJson createdUser) throws Exception {
        GenerateCategory[] categories = generateUser.categories();
        if (categories != null) {
            for (GenerateCategory category : categories) {
                CategoryJson cj = new CategoryJson();
                cj.setUsername(createdUser.getUsername());
                cj.setCategory(category.value());
                createdUser.getCategoryJsons().add(spendClient.createCategory(cj));
            }
        }
    }

    private String getTestId(ExtensionContext context) {
        return Objects.requireNonNull(
                context.getRequiredTestMethod().getAnnotation(AllureId.class)
        ).value();
    }

    public enum Selector {
        METHOD, NESTED;

        public ExtensionContext.Namespace getNamespace() {
            switch (this) {
                case METHOD -> {
                    return ON_METHOD_USERS_NAMESPACE;
                }
                case NESTED -> {
                    return API_LOGIN_USERS_NAMESPACE;
                }
                default -> throw new IllegalStateException();
            }
        }
    }

    private Map<Selector, List<GenerateUser>> extractGenerateUserAnnotations(ExtensionContext context) {
        Map<Selector, List<GenerateUser>> annotationsOnTest = new HashMap<>();
        GenerateUser singleUserAnnotation = context.getRequiredTestMethod().getAnnotation(GenerateUser.class);
        GenerateUsers multipleUserAnnotation = context.getRequiredTestMethod().getAnnotation(GenerateUsers.class);
        if (singleUserAnnotation != null && singleUserAnnotation.handleAnnotation()) {
            annotationsOnTest.put(Selector.METHOD, List.of(singleUserAnnotation));
        } else if (multipleUserAnnotation != null) {
            annotationsOnTest.put(Selector.METHOD, Arrays.asList(multipleUserAnnotation.value()));
        }
        ApiLogin apiLoginAnnotation = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);
        if (apiLoginAnnotation != null && apiLoginAnnotation.nifflerUser().handleAnnotation()) {
            annotationsOnTest.put(Selector.NESTED, List.of(apiLoginAnnotation.nifflerUser()));
        }
        return annotationsOnTest;
    }

    private UserJson apiRegister(String username, String password) throws Exception {
        Response<Void> res = authClient.register(username, password);
        if (res.code() != 201) {
            throw new RuntimeException("User is not registered");
        }
        UserJson currentUser = waitWhileUserToBeConsumed(username, 10000L);
        currentUser.setPassword(password);
        return currentUser;
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
