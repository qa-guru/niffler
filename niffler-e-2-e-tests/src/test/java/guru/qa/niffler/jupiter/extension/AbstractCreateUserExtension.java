package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.NifflerSpendClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.GenerateUsers;
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

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static guru.qa.niffler.utils.DataUtils.generateRandomPassword;
import static guru.qa.niffler.utils.DataUtils.generateRandomUsername;

public abstract class AbstractCreateUserExtension implements BeforeEachCallback, ParameterResolver {

    private final NifflerSpendClient spendClient = new NifflerSpendClient();
    protected static final Config CFG = Config.getConfig();

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
                GenerateUser generateUser = value.get(i);
                String username = generateUser.username();
                String password = generateUser.password();
                if ("".equals(username)) {
                    username = generateRandomUsername();
                }
                if ("".equals(password)) {
                    password = generateRandomPassword();
                }
                UserJson createdUser = createUser(username, password);

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

    private Map<User.Selector, List<GenerateUser>> extractGenerateUserAnnotations(ExtensionContext context) {
        Map<User.Selector, List<GenerateUser>> annotationsOnTest = new HashMap<>();
        GenerateUser singleUserAnnotation = context.getRequiredTestMethod().getAnnotation(GenerateUser.class);
        GenerateUsers multipleUserAnnotation = context.getRequiredTestMethod().getAnnotation(GenerateUsers.class);
        if (singleUserAnnotation != null && singleUserAnnotation.handleAnnotation()) {
            annotationsOnTest.put(User.Selector.METHOD, List.of(singleUserAnnotation));
        } else if (multipleUserAnnotation != null) {
            annotationsOnTest.put(User.Selector.METHOD, Arrays.asList(multipleUserAnnotation.value()));
        }
        ApiLogin apiLoginAnnotation = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);
        if (apiLoginAnnotation != null && apiLoginAnnotation.nifflerUser().handleAnnotation()) {
            annotationsOnTest.put(User.Selector.NESTED, List.of(apiLoginAnnotation.nifflerUser()));
        }
        return annotationsOnTest;
    }

    protected abstract UserJson createUser(String username, String password) throws Exception;

    protected abstract void createIncomeInvitationsIfPresent(GenerateUser generateUser, UserJson createdUser) throws Exception;

    protected abstract void createOutcomeInvitationsIfPresent(GenerateUser generateUser, UserJson createdUser) throws Exception;

    protected abstract void createFriendsIfPresent(GenerateUser generateUser, UserJson createdUser) throws Exception;
}
