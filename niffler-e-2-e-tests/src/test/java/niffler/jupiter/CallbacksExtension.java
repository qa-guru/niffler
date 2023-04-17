package niffler.jupiter;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

public class CallbacksExtension implements
    BeforeAllCallback,
    AfterAllCallback,
    BeforeEachCallback,
    AfterEachCallback,
    BeforeTestExecutionCallback,
    AfterTestExecutionCallback,
    TestExecutionExceptionHandler {

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {
    context.getRequiredTestClass();
    System.out.println("#### BeforeAllCallback!");
  }

  @Override
  public void afterAll(ExtensionContext context) throws Exception {
    System.out.println("#### AfterAllCallback!");
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
//    context.getRequiredTestMethod(); // context for test
//    context.getRoot().getRequiredTestMethod();  // context for test run
//    context.getParent().get().getRequiredTestMethod(); // context for class

    System.out.println("  #### BeforeEachCallback!");
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    System.out.println("  #### AftereEachCallback!");
  }

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {
    System.out.println("          ####  AfterTestExecutionCallback!");
  }

  @Override
  public void beforeTestExecution(ExtensionContext context) throws Exception {
    System.out.println("          #### BeforeTestExecutionCallback!");
  }

  @Override
  public void handleTestExecutionException(ExtensionContext context, Throwable throwable)
      throws Throwable {
    System.out.println("          #### TestExecutionExceptionHandler!");
    throw throwable;
  }
}
