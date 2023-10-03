package guru.qa.niffler.test.hw_part_1;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.platform.commons.PreconditionViolationException;

public class CallbacksExtension implements
      BeforeAllCallback,
      AfterAllCallback,
      BeforeEachCallback,
      AfterEachCallback,
      BeforeTestExecutionCallback,
      AfterTestExecutionCallback,
      TestExecutionExceptionHandler
{

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {

        System.out.println("#### BeforeAllCallback");

        System.out.println("getRequiredTestClass: " + context.getRequiredTestClass());

        try {
            System.out.println("getRequiredTestMethod: " + context.getRequiredTestMethod());
        } catch (PreconditionViolationException e) {
            System.out.println("getRequiredTestMethod is not present in beforeAll");
        }

        try {
            System.out.println("getRequiredTestInstance: " + context.getRequiredTestInstance());
        } catch (PreconditionViolationException e) {
            System.out.println("getRequiredTestInstance is not present in beforeAll");
        }

        try {
            System.out.println("getRequiredTestInstances: " + context.getRequiredTestInstances());
        } catch (PreconditionViolationException e) {
            System.out.println("getRequiredTestInstances is not present in beforeAll");
        }

    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {

        System.out.println("#### AfterAllCallback");

        System.out.println("getRequiredTestClass: " + context.getRequiredTestClass().getName());

        try {
            System.out.println("getRequiredTestMethod: " + context.getRequiredTestMethod().getName());
        } catch (PreconditionViolationException e) {
            System.out.println("getRequiredTestMethod is not present in afterAll");
        }

        try {
            System.out.println("getRequiredTestInstance: " + context.getRequiredTestInstance().getClass().getName());
        } catch (PreconditionViolationException e) {
            System.out.println("getRequiredTestInstance is not present in afterAll");
        }

        try {
            System.out.println("getRequiredTestInstances: " + context.getRequiredTestInstances().getClass().getName());
        } catch (PreconditionViolationException e) {
            System.out.println("getRequiredTestInstances is not present in afterAll");
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        System.out.println("        #### BeforeTestExecutionCallback");

        System.out.println("getRequiredTestClass: " + context.getRequiredTestClass().getName());
        System.out.println("getRequiredTestMethod: " + context.getRequiredTestMethod().getName());
        System.out.println("getRequiredTestInstance: " + context.getRequiredTestInstance().getClass().getName());
        System.out.println("getRequiredTestInstances: " + context.getRequiredTestInstances().getClass().getName());

    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {

        System.out.println("        #### BeforeEachCallback");

        System.out.println("getRequiredTestClass: " + context.getRequiredTestClass().getName());
        System.out.println("getRequiredTestMethod: " + context.getRequiredTestMethod().getName());
        System.out.println("getRequiredTestInstance: " + context.getRequiredTestInstance().getClass().getName());
        System.out.println("getRequiredTestInstances: " + context.getRequiredTestInstances().getClass().getName());
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {

        System.out.println("                #### BeforeTestExecutionCallback");

        System.out.println("getRequiredTestClass: " + context.getRequiredTestClass().getName());
        System.out.println("getRequiredTestMethod: " + context.getRequiredTestMethod().getName());
        System.out.println("getRequiredTestInstance: " + context.getRequiredTestInstance().getClass().getName());
        System.out.println("getRequiredTestInstances: " + context.getRequiredTestInstances().getClass().getName());
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {

        System.out.println("                #### AfterTestExecutionCallback");

        System.out.println("getRequiredTestClass: " + context.getRequiredTestClass().getName());
        System.out.println("getRequiredTestMethod: " + context.getRequiredTestMethod().getName());
        System.out.println("getRequiredTestInstance: " + context.getRequiredTestInstance().getClass().getName());
        System.out.println("getRequiredTestInstances: " + context.getRequiredTestInstances().getClass().getName());
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        System.out.println("                          #### TestExecutionExceptionHandler");
        throw throwable;
    }
}
