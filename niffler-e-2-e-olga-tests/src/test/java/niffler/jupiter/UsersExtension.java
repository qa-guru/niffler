package niffler.jupiter;

import io.qameta.allure.AllureId;
import niffler.model.UserModel;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Parameter;
import java.util.*;
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

        List<Parameter> parameters = new LinkedList<>(Arrays.asList(context.getRequiredTestMethod().getParameters()));
        List<User.UserType> desiredUserTypes = new LinkedList<>();
        for (Parameter parameter : parameters) {
            if (parameter.isAnnotationPresent(User.class)) {
                desiredUserTypes.add(parameter.getAnnotation(User.class).userType());
            }
        }

        Map<User.UserType, UserModel> users = new LinkedHashMap<>();
        for (User.UserType desiredUserType : desiredUserTypes) {
            UserModel user = null;
            while (user == null) {
                if (desiredUserType == User.UserType.ADMIN) {
                    user = USER_MODEL_ADMIN_QUEUE.poll();
                } else {
                    user = USER_MODEL_COMMON_QUEUE.poll();
                }
            }
            Objects.requireNonNull(user);
            users.put(desiredUserType, user);
        }
        context.getStore(NAMESPACE).put(id, users);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        String id = getTestId(context);
        Map<User.UserType, UserModel> users = context.getStore(NAMESPACE).get(id, Map.class);

        for (Map.Entry<User.UserType, UserModel> user : users.entrySet()) {
            if (user.getKey().equals(User.UserType.ADMIN)) {
                USER_MODEL_ADMIN_QUEUE.add(user.getValue());
            } else {
                USER_MODEL_COMMON_QUEUE.add(user.getValue());
            }
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
                .get(parameterContext.getParameter().getDeclaredAnnotation(User.class).userType());
    }
}