package niffler.jupiter.extension;

import io.qameta.allure.AllureId;
import io.qameta.allure.Step;
import niffler.api.NifflerAuthClient;
import niffler.api.NifflerSpendClient;
import niffler.api.NifflerUserdataClient;
import niffler.config.Config;
import niffler.jupiter.annotation.ApiLogin;
import niffler.jupiter.annotation.Friends;
import niffler.jupiter.annotation.GenerateCategory;
import niffler.jupiter.annotation.GenerateSpend;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.Invitations;
import niffler.jupiter.annotation.User;
import niffler.model.rest.CategoryJson;
import niffler.model.rest.FriendJson;
import niffler.model.rest.SpendJson;
import niffler.model.rest.UserJson;
import niffler.utils.DateUtils;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import retrofit2.Response;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static niffler.utils.DataUtils.generateRandomPassword;
import static niffler.utils.DataUtils.generateRandomUsername;

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
        Map<Selector, GenerateUser> userAnnotations = extractGenerateUserAnnotations(context);
        for (Map.Entry<Selector, GenerateUser> entry : userAnnotations.entrySet()) {
            final GenerateUser generateUser = entry.getValue();
            String username = generateUser.username();
            String password = generateUser.password();
            if ("".equals(username)) {
                username = generateRandomUsername();
            }
            if ("".equals(password)) {
                password = generateRandomPassword();
            }
            UserJson userJson = apiRegister(username, password);

            createCategoriesIfPresent(generateUser, userJson);
            createSpendsIfPresent(generateUser, userJson);
            createFriendsIfPresent(generateUser, userJson);
            createInvitationsIfPresent(generateUser, userJson);

            context.getStore(entry.getKey().getNamespace()).put(testId, userJson);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class)
                && parameterContext.getParameter().isAnnotationPresent(User.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final String testId = getTestId(extensionContext);
        User annotation = parameterContext.getParameter().getAnnotation(User.class);
        return extensionContext.getStore(annotation.selector().getNamespace()).get(testId, UserJson.class);
    }

    private void createInvitationsIfPresent(GenerateUser generateUser, UserJson userJson) throws Exception {
        Invitations invitations = generateUser.invitations();
        if (invitations.handleAnnotation() && invitations.count() > 0) {
            for (int i = 0; i < invitations.count(); i++) {
                UserJson invitation = apiRegister(generateRandomUsername(), generateRandomPassword());
                FriendJson addFriend = new FriendJson();
                addFriend.setUsername(userJson.getUsername());
                userdataClient.addFriend(invitation.getUsername(), addFriend);
                userJson.getInvitationsJsons().add(invitation);
            }
        }
    }

    private void createFriendsIfPresent(GenerateUser generateUser, UserJson userJson) throws Exception {
        Friends friends = generateUser.friends();
        if (friends.handleAnnotation() && friends.count() > 0) {
            for (int i = 0; i < friends.count(); i++) {
                UserJson friend = apiRegister(generateRandomUsername(), generateRandomPassword());
                FriendJson addFriend = new FriendJson();
                FriendJson invitation = new FriendJson();
                addFriend.setUsername(friend.getUsername());
                invitation.setUsername(userJson.getUsername());
                userdataClient.addFriend(userJson.getUsername(), addFriend);
                userdataClient.acceptInvitation(friend.getUsername(), invitation);
                userJson.getFriendsJsons().add(friend);
            }
        }
    }

    private void createSpendsIfPresent(GenerateUser generateUser, UserJson userJson) throws Exception {
        GenerateSpend[] spends = generateUser.spends();
        if (spends != null) {
            for (GenerateSpend spend : spends) {
                SpendJson sj = new SpendJson();
                sj.setUsername(userJson.getUsername());
                sj.setCategory(spend.spendCategory());
                sj.setAmount(spend.amount());
                sj.setCurrency(spend.currency());
                sj.setDescription(spend.spendName());
                sj.setSpendDate(DateUtils.addDaysToDate(new Date(), Calendar.DAY_OF_WEEK, spend.addDaysToSpendDate()));
                userJson.getSpendJsons().add(spendClient.createSpend(sj));
            }
        }
    }

    private void createCategoriesIfPresent(GenerateUser generateUser, UserJson userJson) throws Exception {
        GenerateCategory[] categories = generateUser.categories();
        if (categories != null) {
            for (GenerateCategory category : categories) {
                CategoryJson cj = new CategoryJson();
                cj.setUsername(userJson.getUsername());
                cj.setCategory(category.value());
                userJson.getCategoryJsons().add(spendClient.createCategory(cj));
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
                default -> {
                    throw new IllegalStateException();
                }
            }
        }
    }

    private Map<Selector, GenerateUser> extractGenerateUserAnnotations(ExtensionContext context) {
        Map<Selector, GenerateUser> annotationsOnTest = new HashMap<>();
        GenerateUser annotationOnMethod = context.getRequiredTestMethod().getAnnotation(GenerateUser.class);
        if (annotationOnMethod != null && annotationOnMethod.handleAnnotation()) {
            annotationsOnTest.put(Selector.METHOD, annotationOnMethod);
        }
        ApiLogin apiLoginAnnotation = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);
        if (apiLoginAnnotation != null && apiLoginAnnotation.nifflerUser().handleAnnotation()) {
            annotationsOnTest.put(Selector.NESTED, apiLoginAnnotation.nifflerUser());
        }
        return annotationsOnTest;
    }

    private UserJson apiRegister(String username, String password) throws Exception {
        authClient.authorize();
        Response<Void> res = authClient.register(username, password);
        if (res.code() != 201) {
            throw new RuntimeException("User is not registered");
        }
        UserJson currentUser = userdataClient.getCurrentUser(username);
        currentUser.setPassword(password);
        return currentUser;
    }
}
