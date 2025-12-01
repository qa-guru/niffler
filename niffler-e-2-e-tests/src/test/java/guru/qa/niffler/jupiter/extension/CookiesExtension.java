package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.service.ThreadLocalCookieStore;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CookiesExtension implements AfterEachCallback {
  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    ThreadLocalCookieStore.INSTANCE.removeAll();
  }
}
