package niffler.jupiter.extension;

import niffler.data.dao.PostgresSpringJdbcUsersDAO;
import niffler.data.dao.UsersDAO;
import niffler.data.entity.UsersEntity;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserEntityExtension implements BeforeEachCallback, AfterEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(niffler.jupiter.extension.UserExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) throws IllegalAccessException {
        Object testInstance = context.getRequiredTestInstance();
        List<Field> fields = Arrays.stream(testInstance.getClass().getDeclaredFields())
                .filter(f -> f.getType().isAssignableFrom(UsersEntity.class))
                .filter(f -> f.isAnnotationPresent(UserEntity.class)).toList();

        UsersDAO usersDAO = new PostgresSpringJdbcUsersDAO();
        List<UsersEntity> userEntities = new ArrayList<>();

        for (Field field : fields) {
            UserEntity userEntityFields = field.getAnnotation(UserEntity.class);
            UsersEntity originalUsersEntity = usersDAO.getByUsername(userEntityFields.username());
            UsersEntity testedUsersEntity = usersDAO.getByUsername(userEntityFields.username());
            testedUsersEntity.setCurrency(userEntityFields.currency().toString());
            usersDAO.updateUser(testedUsersEntity);
            field.setAccessible(true);
            field.set(testInstance, testedUsersEntity);
            userEntities.add(originalUsersEntity);
        }
        context.getStore(NAMESPACE).put(getTestId(context), userEntities);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        UsersDAO usersDAO = new PostgresSpringJdbcUsersDAO();
        List<UsersEntity> userEntities = context.getStore(NAMESPACE).get(getTestId(context), List.class);
        for (UsersEntity userEntity : userEntities) {
            usersDAO.updateUser(userEntity);
        }
    }

    private String getTestId(ExtensionContext context) {
        return context.getRequiredTestClass().getName() + ": " +
                context.getRequiredTestMethod().getName();
    }
}
