package niffler.jupiter.user;

import niffler.models.user.User;
import niffler.models.user.UserRole;
import org.junit.jupiter.api.extension.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.codeborne.selenide.Selenide.sleep;
import static niffler.models.user.UserRole.*;
import static org.junit.jupiter.api.extension.ExtensionContext.*;

final class UserExtension implements BeforeTestExecutionCallback, ParameterResolver, AfterTestExecutionCallback {

    private static final int GET_USER_TIMEOUT_MINUTES = 3;

    public static final Namespace NAMESPACE = Namespace.create(UserExtension.class);
    private static final Map<UserRole, Queue<User>> USERS_QUEUE = new ConcurrentHashMap<>();

    static {
        Queue<User> admins = new LinkedList<>();
        admins.add(new User("admin1", "admin1"));
        admins.add(new User("admin2", "admin2"));
        admins.add(new User("admin3", "admin3"));
        USERS_QUEUE.put(ADMIN, admins);

        Queue<User> managers = new LinkedList<>();
        managers.add(new User("manager1", "manager1"));
        managers.add(new User("manager2", "manager2"));
        USERS_QUEUE.put(MANAGER, managers);

        Queue<User> commons = new LinkedList<>();
        commons.add(new User("common1", "common1"));
        commons.add(new User("common2", "common2"));
        commons.add(new User("common3", "common3"));
        USERS_QUEUE.put(COMMON, commons);
    }

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) {
        List<UserRole> userRoles = Arrays.stream(extensionContext.getRequiredTestMethod().getParameters())
                .filter(p -> p.isAnnotationPresent(WithUser.class))
                .filter(p -> p.getType().isAssignableFrom(User.class))
                .map(p -> p.getAnnotation(WithUser.class).value())
                .toList();

        Map<UserRole, List<User>> users = new HashMap<>();
        new HashSet<>(userRoles).forEach(role -> users.put(role, new ArrayList<>()));

        for (UserRole role : userRoles) {
            User user = pollFromUsersQueue(role);
            users.get(role).add(user);
        }

        String testIdentifier = getTestIdentifier(extensionContext);
        extensionContext.getStore(NAMESPACE).put(testIdentifier, users);
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
        String testIdentifier = getTestIdentifier(extensionContext);
        UserRole role = parameterContext.getParameter().getAnnotation(WithUser.class).value();

        Map<UserRole, List<User>> users = extensionContext.getStore(NAMESPACE).get(testIdentifier, Map.class);
        for (User user : users.get(role)) {
            if (!user.isAvailable())
                continue;

            user.setAvailable(false);
            return user;
        }

        throw new RuntimeException("No user found with role " + role);
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        String testIdentifier = getTestIdentifier(extensionContext);

        Map<UserRole, List<User>> usersMap = extensionContext.getStore(NAMESPACE).get(testIdentifier, Map.class);
        for (UserRole role : usersMap.keySet()) {
            usersMap.get(role).forEach(user -> {
                user.setAvailable(true);
                USERS_QUEUE.get(role).add(user);
            });
        }
    }

    private String getTestIdentifier(ExtensionContext extensionContext) {
        return extensionContext.getRequiredTestClass().getName() + ":" +
                extensionContext.getRequiredTestMethod().getName();
    }

    private User pollFromUsersQueue(UserRole role) {
        if (!USERS_QUEUE.containsKey(role))
            throw new RuntimeException("There are no users with role " + role);

        Queue<User> queue = USERS_QUEUE.get(role);
        for (int i = 1; i <= GET_USER_TIMEOUT_MINUTES * 60; i++) {
            User user = queue.poll();
            if (user != null)
                return user;
            sleep(1_000);
        }

        throw new RuntimeException("No user found with role " + role);
    }

}
