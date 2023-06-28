package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.dao.PostgresHibernateUsersDAO;
import guru.qa.niffler.data.dao.PostgresSpringJdbcUsersDAO;
import guru.qa.niffler.jupiter.annotation.DAO;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class DAOResolver implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        List<Field> fields = Arrays.stream(testInstance.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(DAO.class))
                .peek(field -> field.setAccessible(true))
                .toList();

        for (Field field : fields) {
            field.set(testInstance, System.getProperty("dao", "jpa").equals("jpa")
                    ? new PostgresHibernateUsersDAO()
                    : new PostgresSpringJdbcUsersDAO());
        }
    }
}
