package guru.qa.niffler.jupiter;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;

public interface AroundAllTestsExtension extends BeforeAllCallback {

  default void beforeAllTests(ExtensionContext context) {
  }

  default void afterAllTests() {
  }

  @Override
  default void beforeAll(ExtensionContext context) throws Exception {
    context.getRoot().getStore(Namespace.GLOBAL).getOrComputeIfAbsent(this.getClass(),
        k -> {
          beforeAllTests(context);
          return (CloseableResource) this::afterAllTests;
        }
    );
  }

}
