package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.context.CookieHolder;
import guru.qa.niffler.api.context.SessionStorageHolder;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ClearCookiesAndSessionExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) {
        CookieHolder.getInstance().flushAll();
        SessionStorageHolder.getInstance().flushAll();
    }
}
