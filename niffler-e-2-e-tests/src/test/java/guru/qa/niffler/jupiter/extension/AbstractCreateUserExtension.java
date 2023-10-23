package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendRestClient;
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
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.utils.DateUtils;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static guru.qa.niffler.utils.DataUtils.generateRandomPassword;
import static guru.qa.niffler.utils.DataUtils.generateRandomUsername;

public abstract class AbstractCreateUserExtension implements BeforeEachCallback, ParameterResolver {

    protected static final Config CFG = Config.getConfig();

    private final SpendRestClient spendClient = new SpendRestClient();

    public static final ExtensionContext.Namespace
            ON_METHOD_USERS_NAMESPACE = ExtensionContext.Namespace.create(AbstractCreateUserExtension.class, User.Selector.METHOD),
            API_LOGIN_USERS_NAMESPACE = ExtensionContext.Namespace.create(AbstractCreateUserExtension.class, User.Selector.NESTED);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        final String testId = getTestId(context);
        Map<User.Selector, List<GenerateUser>> userAnnotations = extractGenerateUserAnnotations(context);
        for (Map.Entry<User.Selector, List<GenerateUser>> entry : userAnnotations.entrySet()) {
            UserJson[] resultCollector = new UserJson[entry.getValue().size()];
            List<GenerateUser> value = entry.getValue();
            for (int i = 0; i < value.size(); i++) {
                GenerateUser user = value.get(i);
                String username = user.username();
                String password = user.password();
                if ("".equals(username)) {
                    username = generateRandomUsername();
                }
                if ("".equals(password)) {
                    password = generateRandomPassword();
                }
                UserJson createdUser = createUser(username, password);

                createCategoriesIfPresent(user.categories(), createdUser);
                createSpendsIfPresent(user.spends(), createdUser);
                createFriendsIfPresent(user.friends(), createdUser);
                createIncomeInvitationsIfPresent(user.incomeInvitations(), createdUser);
                createOutcomeInvitationsIfPresent(user.outcomeInvitations(), createdUser);
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
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), User.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final String testId = getTestId(extensionContext);
        User annotation = parameterContext.getParameter().getAnnotation(User.class);
        return extensionContext.getStore(annotation.selector().getNamespace()).get(testId);
    }

    private void createSpendsIfPresent(@Nullable GenerateSpend[] spends, @Nonnull UserJson createdUser) throws Exception {
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
                createdUser.testData().spendJsons().add(spendClient.createSpend(sj));
            }
        }
    }

    private void createCategoriesIfPresent(@Nullable GenerateCategory[] categories, UserJson createdUser) throws Exception {
        if (categories != null) {
            for (GenerateCategory category : categories) {
                CategoryJson cj = new CategoryJson(null, category.value(), createdUser.username());
                createdUser.testData().categoryJsons().add(spendClient.createCategory(cj));
            }
        }
    }

    private String getTestId(ExtensionContext context) {
        return AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                AllureId.class
        ).orElseThrow().value();
    }

    private Map<User.Selector, List<GenerateUser>> extractGenerateUserAnnotations(@Nonnull ExtensionContext context) {
        Map<User.Selector, List<GenerateUser>> annotationsOnTest = new HashMap<>();
        GenerateUser singleUserAnnotation = context.getRequiredTestMethod().getAnnotation(GenerateUser.class);
        GenerateUsers multipleUserAnnotation = context.getRequiredTestMethod().getAnnotation(GenerateUsers.class);
        if (singleUserAnnotation != null && singleUserAnnotation.handleAnnotation()) {
            annotationsOnTest.put(User.Selector.METHOD, List.of(singleUserAnnotation));
        } else if (multipleUserAnnotation != null) {
            annotationsOnTest.put(User.Selector.METHOD, Arrays.asList(multipleUserAnnotation.value()));
        }
        ApiLogin apiLoginAnnotation = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);
        if (apiLoginAnnotation != null && apiLoginAnnotation.user().handleAnnotation()) {
            annotationsOnTest.put(User.Selector.NESTED, List.of(apiLoginAnnotation.user()));
        }
        return annotationsOnTest;
    }

    protected abstract UserJson createUser(@Nonnull String username,
                                           @Nonnull String password) throws Exception;

    protected abstract void createIncomeInvitationsIfPresent(@Nonnull IncomeInvitations incomeInvitations,
                                                             @Nonnull UserJson createdUser) throws Exception;

    protected abstract void createOutcomeInvitationsIfPresent(@Nonnull OutcomeInvitations outcomeInvitations,
                                                              @Nonnull UserJson createdUser) throws Exception;

    protected abstract void createFriendsIfPresent(@Nonnull Friends friends,
                                                   @Nonnull UserJson createdUser) throws Exception;
}
