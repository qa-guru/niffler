package niffler.jupiter;

import niffler.data.jpa.query.PostgresHibernateFriends;
import niffler.data.jpa.query.PostgresHibernateUsers;
import niffler.data.jpa.query.PostgresSpringJdbcUsers;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DAOResolver implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        List<Field> fields = Arrays.stream(testInstance.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(DAO.class))
                .peek(field -> field.setAccessible(true))
                .collect(Collectors.toList());


        for (Field field : fields) {
            if (field.getName().contains("users")) {

                field.set(testInstance, System.getProperty("dao", "jpa").equals("jpa")
                        ? new PostgresHibernateUsers()
                        : new PostgresSpringJdbcUsers());
            } else {

                field.set(testInstance, System.getProperty("dao", "jpa").equals("jpa")
                        ? new PostgresHibernateFriends()
                        : new PostgresSpringJdbcUsers());

            }
        }

    }
}
