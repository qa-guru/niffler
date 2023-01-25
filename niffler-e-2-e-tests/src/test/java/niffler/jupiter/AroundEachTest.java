package niffler.jupiter;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public interface AroundEachTest extends BeforeEachCallback {

    @Override
   default void beforeEach(ExtensionContext context) {
        context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL).
                getOrComputeIfAbsent(this.getClass(),
                        k -> {
                            beforeEachTests();
                            return (ExtensionContext.Store.CloseableResource) this::afterEachTests;
                        }
                );
    }

    default void afterEachTests() {
    }
   default void beforeEachTests() {}
}
