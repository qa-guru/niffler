package niffler.jupiter;

import niffler.model.UserModel;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import java.util.*;
import java.lang.reflect.Parameter;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UserExtension implements
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(UserExtension.class);

    private static final Queue<UserModel> USER_MODEL_ADMIN_QUEUE = new ConcurrentLinkedQueue<>();
    private static final Queue<UserModel> USER_MODEL_COMMON_QUEUE = new ConcurrentLinkedQueue<>();

    static {
        USER_MODEL_ADMIN_QUEUE.add(new UserModel("dima", "12345"));
        USER_MODEL_COMMON_QUEUE.add(new UserModel("bill", "12345"));
        USER_MODEL_COMMON_QUEUE.add(new UserModel("test", "test"));
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        String id = getTestId(context);

        List<Parameter> parameters = new LinkedList<>(Arrays.asList(context.getRequiredTestMethod().getParameters()));
        Map<String, User.UserType> desiredUserTypes = new HashMap<>();
        for (Parameter parameter : parameters) {
            if (parameter.isAnnotationPresent(User.class)) {
                desiredUserTypes.put(parameter.getName(), parameter.getAnnotation(User.class).userType());
            }
        }

        Map<String, UserModel> users = new LinkedHashMap<>();
        for (Map.Entry<String, User.UserType> userType : desiredUserTypes.entrySet()) {
            UserModel user = null;
            while (user == null) {
                if (userType.getValue() == User.UserType.ADMIN) {
                    user = USER_MODEL_ADMIN_QUEUE.poll();
                } else {
                    user = USER_MODEL_COMMON_QUEUE.poll();
                }
            }
            Objects.requireNonNull(user);
            users.put(userType.getKey(), user);
        }
        context.getStore(NAMESPACE).put(id, users);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        String id = getTestId(context);
        Map<String, UserModel> users = context.getStore(NAMESPACE).get(id, Map.class);

        List<Parameter> parameters = new LinkedList<>(Arrays.asList(context.getRequiredTestMethod().getParameters()));
        for (Parameter parameter : parameters) {
            if (parameter.isAnnotationPresent(User.class)) {
                UserModel user = users.get(parameter.getName());
                if (parameter.getAnnotation(User.class).userType().equals(User.UserType.ADMIN)) {
                    USER_MODEL_ADMIN_QUEUE.add(user);
                } else {
                    USER_MODEL_COMMON_QUEUE.add(user);
                }
            }
        }
    }

    private String getTestId(ExtensionContext context) {
        return context.getRequiredTestClass().getName() + ": " +
                context.getRequiredTestMethod().getName();
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
                .get(parameterContext.getParameter().getName());
    }
}
