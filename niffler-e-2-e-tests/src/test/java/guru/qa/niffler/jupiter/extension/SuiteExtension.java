package guru.qa.niffler.jupiter.extension;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public interface SuiteExtension extends BeforeAllCallback {

  default void beforeSuite(ExtensionContext context) {
  }

  default void afterSuite() {
  }

  @Override
  default void beforeAll(ExtensionContext context) {
    context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL).
        getOrComputeIfAbsent(this.getClass(),
            k -> {
              beforeSuite(context);
              return (ExtensionContext.Store.CloseableResource) this::afterSuite;
            }
        );
  }
}
