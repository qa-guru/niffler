package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.StaticUser;
import guru.qa.niffler.model.queue.UserModel;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UsersQueueExtension implements
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    private static final Queue<UserModel> USER_MODEL_ADMIN_QUEUE = new ConcurrentLinkedQueue<>();
    private static final Queue<UserModel> USER_MODEL_COMMON_QUEUE = new ConcurrentLinkedQueue<>();

    static {
        USER_MODEL_ADMIN_QUEUE.add(new UserModel("dima", "12345"));
        USER_MODEL_COMMON_QUEUE.add(new UserModel("bill", "12345"));
        USER_MODEL_COMMON_QUEUE.add(new UserModel("test", "test"));
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        StaticUser.Type desiredUserType = Arrays.stream(context.getRequiredTestMethod()
                        .getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, StaticUser.class))
                .map(p -> p.getAnnotation(StaticUser.class))
                .findFirst()
                .orElseThrow()
                .value();

        UserModel user = null;
        while (user == null) {
            if (desiredUserType == StaticUser.Type.ADMIN) {
                user = USER_MODEL_ADMIN_QUEUE.poll();
            } else {
                user = USER_MODEL_COMMON_QUEUE.poll();
            }
        }
        Objects.requireNonNull(user);
        context.getStore(NAMESPACE).put(
                context.getUniqueId(),
                Map.of(desiredUserType, user)
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public void afterTestExecution(ExtensionContext context) {
        Map<StaticUser.Type, UserModel> map = context.getStore(NAMESPACE).get(
                context.getUniqueId(),
                Map.class
        );
        if (map.containsKey(StaticUser.Type.ADMIN)) {
            USER_MODEL_ADMIN_QUEUE.add(map.get(StaticUser.Type.ADMIN));
        } else {
            USER_MODEL_COMMON_QUEUE.add(map.get(StaticUser.Type.COMMON));
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserModel.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), StaticUser.class);
    }

    @Override
    public UserModel resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return (UserModel) extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class)
                .values()
                .iterator()
                .next();
    }
}
