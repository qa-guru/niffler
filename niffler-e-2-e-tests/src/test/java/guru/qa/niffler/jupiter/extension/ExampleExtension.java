package guru.qa.niffler.jupiter.extension;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ExampleExtension implements AfterAllCallback,
        AfterEachCallback,
        BeforeAllCallback,
        BeforeEachCallback,
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback {

    @Override
    public void afterAll(ExtensionContext context) {
        System.out.println("### AfterAllCallback");
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        System.out.println("      ### BeforeEachCallback");
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        System.out.println("### BeforeAllCallback");
    }

    @Override
    public void afterEach(ExtensionContext context) {
        System.out.println("      ### AfterEachCallback");
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        System.out.println("              ### BeforeTestExecutionCallback");
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        System.out.println("               ### AfterTestExecutionCallback");
    }
}
