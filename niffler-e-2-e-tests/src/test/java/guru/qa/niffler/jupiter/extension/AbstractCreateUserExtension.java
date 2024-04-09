package guru.qa.niffler.jupiter.extension;

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
import guru.qa.niffler.model.rest.UserJson;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static guru.qa.niffler.utils.DataUtils.generateRandomPassword;
import static guru.qa.niffler.utils.DataUtils.generateRandomUsername;

public abstract class AbstractCreateUserExtension implements BeforeEachCallback, ParameterResolver {

    protected static final Config CFG = Config.getConfig();

    public static final ExtensionContext.Namespace
            NAMESPACE = ExtensionContext.Namespace.create(AbstractCreateUserExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Map<User.Selector, List<UserJson>> createdUsers = new HashMap<>();

        for (Map.Entry<User.Selector, List<GenerateUser>> entry : extractGenerateUserAnnotations(context).entrySet()) {
            List<UserJson> usersForSelector = new ArrayList<>();

            for (GenerateUser user : entry.getValue()) {
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
                usersForSelector.add(createdUser);
            }
            createdUsers.put(entry.getKey(), usersForSelector);
        }

        context.getStore(NAMESPACE).put(
                context.getUniqueId(),
                createdUsers
        );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> parameterType = parameterContext.getParameter().getType();
        return (parameterType.isAssignableFrom(UserJson.class) || parameterType.isAssignableFrom(UserJson[].class))
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), User.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        User annotation = parameterContext.getParameter().getAnnotation(User.class);
        List<UserJson> storedUsers = createdUsers(extensionContext, annotation.selector());

        return parameterContext.getParameter().getType().isAssignableFrom(UserJson[].class)
                ? storedUsers.toArray(new UserJson[0])
                : storedUsers.getFirst();
    }

    @SuppressWarnings("unchecked")
    public static List<UserJson> createdUsers(ExtensionContext context, User.Selector selector) {
        return (List<UserJson>) context.getStore(NAMESPACE)
                .get(context.getUniqueId(), Map.class)
                .get(selector);
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

    protected abstract void createSpendsIfPresent(@Nullable GenerateSpend[] spends,
                                                  @Nonnull UserJson createdUser) throws Exception;

    protected abstract void createCategoriesIfPresent(@Nullable GenerateCategory[] categories,
                                                      @Nonnull UserJson createdUser) throws Exception;
}
