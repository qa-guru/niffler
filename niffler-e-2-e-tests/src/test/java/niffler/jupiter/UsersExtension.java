package niffler.jupiter;

import io.qameta.allure.AllureId;
import niffler.model.UserModel;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UsersExtension implements
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(UsersExtension.class);

    private static final Queue<UserModel> USER_MODEL_ADMIN_QUEUE = new ConcurrentLinkedQueue<>();
    private static final Queue<UserModel> USER_MODEL_COMMON_QUEUE = new ConcurrentLinkedQueue<>();

    static {
        USER_MODEL_ADMIN_QUEUE.add(new UserModel("dima", "12345"));
        USER_MODEL_COMMON_QUEUE.add(new UserModel("bill", "12345"));
        USER_MODEL_COMMON_QUEUE.add(new UserModel("test", "test"));
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        String id = getTestId(context);
        User.UserType desiredUserType = Arrays.stream(context.getRequiredTestMethod()
                        .getParameters())
                .filter(p -> p.isAnnotationPresent(User.class))
                .map(p -> p.getAnnotation(User.class))
                .findFirst()
                .orElseThrow()
                .userType();

        UserModel user = null;
        while (user == null) {
            if (desiredUserType == User.UserType.ADMIN) {
                user = USER_MODEL_ADMIN_QUEUE.poll();
            } else {
                user = USER_MODEL_COMMON_QUEUE.poll();
            }
        }
        Objects.requireNonNull(user);
        context.getStore(NAMESPACE).put(id, Map.of(desiredUserType, user));
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        String id = getTestId(context);
        Map<User.UserType, UserModel> map = context.getStore(NAMESPACE).get(id, Map.class);
        if (map.containsKey(User.UserType.ADMIN)) {
            USER_MODEL_ADMIN_QUEUE.add(map.get(User.UserType.ADMIN));
        } else {
            USER_MODEL_COMMON_QUEUE.add(map.get(User.UserType.COMMON));
        }
    }

    private String getTestId(ExtensionContext context) {
        return Objects.requireNonNull(
                context.getRequiredTestMethod().getAnnotation(AllureId.class)
        ).value();
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserModel.class)
                && parameterContext.getParameter().isAnnotationPresent(User.class);
    }

    @Override
    public UserModel resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        String id = getTestId(extensionContext);
        return (UserModel) extensionContext.getStore(NAMESPACE).get(id, Map.class)
                .values()
                .iterator()
                .next();
    }
}
