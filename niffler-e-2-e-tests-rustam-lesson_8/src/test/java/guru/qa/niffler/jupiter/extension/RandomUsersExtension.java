package guru.qa.niffler.jupiter.extension;


import com.github.javafaker.Faker;
import guru.qa.niffler.db.dao.NifflerUserRepository;
import guru.qa.niffler.db.entity.auth.Authority;
import guru.qa.niffler.db.entity.auth.AuthorityEntity;
import guru.qa.niffler.db.entity.auth.UserEntity;
import guru.qa.niffler.db.entity.userdata.UserDataEntity;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class RandomUsersExtension implements
    BeforeEachCallback,
    AfterTestExecutionCallback,
    ParameterResolver
{

//  private final AuthUserDAO authUserDAO = AuthUserDAO.getImpl();
//  private final UserDataUserDAO userDataUserDAO = UserDataUserDAO.getImpl();
//  private final NifflerUserRepository userRepository = new NifflerUserRepository();
//  private NifflerUserRepository userRepository;
//  private UserEntity user;
//  private UserDataEntity userData;

  public static Namespace RANDOM_USER_NAMESPACE = Namespace.create(RandomUsersExtension.class);
  public static Namespace RANDOM_USER_REPOSITORY_NAMESPACE = Namespace.create(RandomUsersExtension.class);

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    GenerateUser annotation = context.getRequiredTestMethod().getAnnotation(GenerateUser.class);
    if (annotation != null) {
      UserEntity user = new UserEntity();
//      user.setUsername(new Faker().name().username());
      user.setUsername("new Faker().name().username()");
      user.setPassword("12345");
      user.setEnabled(true);
      user.setAccountNonExpired(true);
      user.setAccountNonLocked(true);
      user.setCredentialsNonExpired(true);
      user.setAuthorities(Arrays.stream(Authority.values())
          .map(a -> {
            AuthorityEntity ae = new AuthorityEntity();
            ae.setAuthority(a);
            ae.setUser(user);
            return ae;
          }).collect(Collectors.toList())
      );

//      List<AuthorityEntity> authorityEntityList = new ArrayList<>();
//      List<Authority> values = List.of(Authority.values());
//      for (Authority value : values) {
//        AuthorityEntity ae = new AuthorityEntity();
//        ae.setAuthority(value);
//        ae.setUser(user);
//
//        authorityEntityList.add(ae);
//      }
//      user.setAuthorities(authorityEntityList);

//      authUserDAO.createUser(user);
      NifflerUserRepository userRepository = new NifflerUserRepository();
      userRepository.createUserForTest(user);

//      userData = new UserDataEntity();
//      userData.setUsername(user.getUsername());
//      userData.setCurrency(CurrencyValues.USD);
//      userData.setFirstname("updated_firstname_2");
//      userData.setSurname("updated_surname_2");
//      userData.setPhoto("photos/photo2.jpeg".getBytes());

//      userDataUserDAO.createUserInUserData(userData);

      context.getStore(RANDOM_USER_NAMESPACE).put("randomUser", user);
      context.getStore(RANDOM_USER_REPOSITORY_NAMESPACE).put("userRepository", userRepository);
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
//    authUserDAO.getUserById(user.getId());
//    userDataUserDAO.getUserdataInUserData(userData);

//    user.setEnabled(false);
////    authUserDAO.updateUser(user);
//
//    userData.setCurrency(CurrencyValues.KZT);
//    userDataUserDAO.updateUserInUserData(userData);
//
//    userDataUserDAO.deleteUserByUsernameInUserData(userData);
//    authUserDAO.deleteUser(user);
    NifflerUserRepository userRepository = context.getStore(RANDOM_USER_REPOSITORY_NAMESPACE).get("userRepository", NifflerUserRepository.class);
    UserEntity user = context.getStore(RANDOM_USER_NAMESPACE).get("randomUser", UserEntity.class);
    userRepository.removeAfterTest(user);
  }

}
