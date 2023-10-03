package guru.qa.niffler.jupiter.extension;


import com.github.javafaker.Faker;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.AuthUserDAOHibernate;
import guru.qa.niffler.db.dao.AuthUserDAOJdbc;
import guru.qa.niffler.db.dao.AuthUserDAOSpringJdbc;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.jupiter.annotation.DAO;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import java.lang.reflect.Field;
import java.util.Arrays;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

public class RandomUserExtension implements
//    TestInstancePostProcessor,
    BeforeEachCallback,
    AfterTestExecutionCallback,
    ParameterResolver
{

//  @DAO
//  private AuthUserDAO authUserDAO = AuthUserDAO.getImpl();
////  @DAO
//  private UserDataUserDAO userDataUserDAO = UserDataUserDAO.getImpl();
  private UserEntity user;

  public static Namespace RANDOM_USER_NAMESPACE = Namespace.create(RandomUserExtension.class);

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    GenerateUser annotation = context.getRequiredTestMethod().getAnnotation(GenerateUser.class);
    if (annotation != null) {
      user = new UserEntity();
      user.setUsername(new Faker().name().username());
      user.setPassword("12345");
      user.setEnabled(true);
      user.setAccountNonExpired(true);
      user.setAccountNonLocked(true);
      user.setCredentialsNonExpired(true);
      user.setAuthorities(Arrays.stream(Authority.values())
          .map(a -> {
            AuthorityEntity ae = new AuthorityEntity();
            ae.setAuthority(a);
            return ae;
          }).toList()
      );
      Object testInstance = context.getRequiredTestInstance();
      Field resultField1 = testInstance.getClass().getDeclaredField("authUserDAO");
      resultField1.setAccessible(true);
      AuthUserDAO authUserDAO = (AuthUserDAO) resultField1.get(testInstance);
      Field resultField2 = testInstance.getClass().getDeclaredField("userDataUserDAO");
      resultField2.setAccessible(true);
      UserDataUserDAO userDataUserDAO = (UserDataUserDAO) resultField2.get(testInstance);

      authUserDAO.createUser(user);
      userDataUserDAO.createUserInUserData(user);

      context.getStore(RANDOM_USER_NAMESPACE).put("randomUser", user);
    }

  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(UserEntity.class);
  }

  @Override
  public UserEntity resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(RANDOM_USER_NAMESPACE).get("randomUser", UserEntity.class);
  }

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {
    Object testInstance = context.getRequiredTestInstance();
    Field resultField1 = testInstance.getClass().getDeclaredField("authUserDAO");
    resultField1.setAccessible(true);
    AuthUserDAO authUserDAO = (AuthUserDAO) resultField1.get(testInstance);
    Field resultField2 = testInstance.getClass().getDeclaredField("userDataUserDAO");
    resultField2.setAccessible(true);
    UserDataUserDAO userDataUserDAO = (UserDataUserDAO) resultField2.get(testInstance);

    userDataUserDAO.deleteUserByIdInUserData(user);
    authUserDAO.deleteUserById(user.getId());
  }

}
