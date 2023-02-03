package niffler.jupiter.user;

import niffler.models.user.User;
import niffler.models.user.UsersQueue;
import org.junit.jupiter.api.extension.*;

import java.util.*;

import static niffler.models.user.UserRole.*;
import static org.junit.jupiter.api.extension.ExtensionContext.*;

final class UserExtension implements BeforeTestExecutionCallback, ParameterResolver, AfterTestExecutionCallback {

    public static final Namespace MAIN_NAMESPACE = Namespace.create(UserExtension.class);
    private static final Namespace TEMP_NAMESPACE = Namespace.create(UserExtension.class, "TEMP");

    private static final UsersQueue USERS_QUEUE = UsersQueue.instance();

    static {
        USERS_QUEUE.add(new User("admin1", "admin1", ADMIN));
        USERS_QUEUE.add(new User("admin2", "admin2", ADMIN));
        USERS_QUEUE.add(new User("admin3", "admin3", ADMIN));

        USERS_QUEUE.add(new User("manager1", "manager1", MANAGER));
        USERS_QUEUE.add(new User("manager2", "manager2", MANAGER));

        USERS_QUEUE.add(new User("common1", "common1", COMMON));
        USERS_QUEUE.add(new User("common2", "common2", COMMON));
        USERS_QUEUE.add(new User("common3", "common3", COMMON));
    }

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) {
        List<WithUser> annotations = Arrays.stream(extensionContext.getRequiredTestMethod().getParameters())
                .filter(p -> p.isAnnotationPresent(WithUser.class))
                .filter(p -> p.getType().isAssignableFrom(User.class))
                .map(p -> p.getAnnotation(WithUser.class))
                .toList();

        Set<User> users = new HashSet<>();
        for (WithUser annotation : annotations)
            users.add(USERS_QUEUE.get(annotation.value()));

        String testIdentifier = getTestIdentifier(extensionContext);
        extensionContext.getStore(MAIN_NAMESPACE).put(testIdentifier, UsersQueue.instance(users));
        extensionContext.getStore(TEMP_NAMESPACE).put(testIdentifier, UsersQueue.instance(users));
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(User.class)
                && parameterContext.getParameter().isAnnotationPresent(WithUser.class);
    }

    @Override
    public User resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        WithUser annotation = parameterContext.getParameter().getAnnotation(WithUser.class);
        return extensionContext.getStore(TEMP_NAMESPACE)
                .get(getTestIdentifier(extensionContext), UsersQueue.class)
                .get(annotation.value());
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        UsersQueue usersQueue = extensionContext.getStore(MAIN_NAMESPACE)
                .get(getTestIdentifier(extensionContext), UsersQueue.class);
        USERS_QUEUE.addAll(usersQueue);
    }

    private String getTestIdentifier(ExtensionContext extensionContext) {
        return extensionContext.getRequiredTestClass().getName() + ":" +
                extensionContext.getRequiredTestMethod().getName();
    }

}
