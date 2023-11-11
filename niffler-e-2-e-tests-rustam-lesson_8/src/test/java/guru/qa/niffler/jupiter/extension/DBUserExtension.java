package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.client.DBUser7RestClient;
import guru.qa.niffler.api.client.RegisterClient;
import guru.qa.niffler.api.context.CookieContext;
import guru.qa.niffler.api.context.SessionContext;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import java.util.Objects;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class DBUserExtension implements BeforeEachCallback, ParameterResolver,
    AfterTestExecutionCallback {

  public final static Namespace NAMESPACE = Namespace.create(DBUserExtension.class);

  private final DBUser7RestClient dbUser7RestClient = new DBUser7RestClient();

  private final RegisterClient registerClient = new RegisterClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    final String testId = getTestId(context);
    DBUser annotation = context.getRequiredTestMethod().getAnnotation(DBUser.class);
    if (annotation != null) {
      UserJson registeredUser = doRegister(
          annotation.username(),
          annotation.password(),
          annotation.submitPassword()
      );

      context.getStore(NAMESPACE).put(testId, registeredUser);
    }
  }

  private UserJson doRegister(String username, String password, String submitPassword) {
    registerClient.registerPreRequest();
    registerClient.register(username, password, submitPassword);
    return dbUser7RestClient.getCurrentUser(username);
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
  }

  @Override
  public UserJson resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    final String testId = getTestId(extensionContext);
    return extensionContext.getStore(NAMESPACE).get(testId, UserJson.class);
  }

  private String getTestId(ExtensionContext context) {
    return Objects.requireNonNull(
        context.getRequiredTestMethod().getAnnotation(AllureId.class)
    ).value();
  }

  @Override
  public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
    SessionContext.getInstance().release();
    CookieContext.getInstance().release();
  }
}
