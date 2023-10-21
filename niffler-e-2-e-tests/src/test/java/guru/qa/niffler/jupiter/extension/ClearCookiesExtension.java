package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.service.ThreadLocalCookieStore;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ClearCookiesExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) {
        ThreadLocalCookieStore.INSTANCE.removeAll();
    }
}
