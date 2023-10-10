package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.jupiter.annotation.DAO;
import java.lang.reflect.Field;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

public class DaoExtension implements TestInstancePostProcessor {

  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext context)
      throws Exception {
    for (Field field: testInstance.getClass().getDeclaredFields()) {
      if (field.getType().isAssignableFrom(AuthUserDAO.class)
          && field.isAnnotationPresent(DAO.class)) {
        field.setAccessible(true);

        field.set(testInstance, AuthUserDAO.getImpl());
      }
      if (field.getType().isAssignableFrom(UserDataUserDAO.class)
          && field.isAnnotationPresent(DAO.class)) {
        field.setAccessible(true);

        field.set(testInstance, UserDataUserDAO.getImpl());
      }

    }
  }
}
