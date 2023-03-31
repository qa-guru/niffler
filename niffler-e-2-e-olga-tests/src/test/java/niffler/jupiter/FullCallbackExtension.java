package niffler.jupiter;

import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.PreconditionViolationException;

public class FullCallbackExtension implements AfterAllCallback,
        AfterEachCallback,
        BeforeAllCallback,
        BeforeEachCallback,
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback {

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        System.out.println("### AfterAllCallback");
        try {
            System.out.println("   getRequiredTestMethod: " + context.getRequiredTestMethod().getName());
        } catch (PreconditionViolationException e) {
            System.out.println("   getRequiredTestMethod is not present in AfterAllCallback");
        }
        try {
            System.out.println("   getRequiredTestClass: " + context.getRequiredTestClass().getName());
        } catch (PreconditionViolationException e) {
            System.out.println("   getRequiredTestClass is not present in AfterAllCallback");
        }
        try {
            System.out.println("   getRequiredTestInstance: " + context.getRequiredTestInstance().getClass().getName());
        } catch (PreconditionViolationException e) {
            System.out.println("   getRequiredTestInstance is not present in AfterAllCallback");
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        System.out.println("      ### BeforeEachCallback");
        System.out.println("         getRequiredTestMethod: " + context.getRequiredTestMethod().getName());
        System.out.println("         getRequiredTestClass: " + context.getRequiredTestClass().getName());
        System.out.println("         getRequiredTestInstance: " + context.getRequiredTestInstance().getClass().getName());
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        System.out.println("### BeforeAllCallback");
        try {
            System.out.println("   getRequiredTestMethod: " + context.getRequiredTestMethod().getName());
        } catch (PreconditionViolationException e) {
            System.out.println("   getRequiredTestMethod is not present in BeforeAllCallback");
        }
        try {
            System.out.println("   getRequiredTestClass: " + context.getRequiredTestClass().getName());
        } catch (PreconditionViolationException e) {
            System.out.println("   getRequiredTestClass is not present in BeforeAllCallback");
        }
        try {
            System.out.println("   getRequiredTestInstance: " + context.getRequiredTestInstance().getClass().getName());
        } catch (PreconditionViolationException e) {
            System.out.println("   getRequiredTestInstance is not present in BeforeAllCallback");
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        System.out.println("      ### AfterEachCallback");
        System.out.println("         getRequiredTestMethod: " + context.getRequiredTestMethod().getName());
        System.out.println("         getRequiredTestClass: " + context.getRequiredTestClass().getName());
        System.out.println("         getRequiredTestInstance: " + context.getRequiredTestInstance().getClass().getName());
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        System.out.println("              ### BeforeTestExecutionCallback");
        System.out.println("                 getRequiredTestMethod: " + context.getRequiredTestMethod().getName());
        System.out.println("                 getRequiredTestClass: " + context.getRequiredTestClass().getName());
        System.out.println("                 getRequiredTestInstance: " + context.getRequiredTestInstance().getClass().getName());
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        System.out.println("               ### AfterTestExecutionCallback");
        System.out.println("                  getRequiredTestMethod: " + context.getRequiredTestMethod().getName());
        System.out.println("                  getRequiredTestClass: " + context.getRequiredTestClass().getName());
        System.out.println("                  getRequiredTestInstance: " + context.getRequiredTestInstance().getClass().getName());
    }
}