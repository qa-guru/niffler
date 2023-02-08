package niffler.jupiter;

import jakarta.persistence.EntityManagerFactory;
import niffler.data.jpa.EmfContext;
import org.junit.jupiter.api.extension.ExtensionContext;

public class JpaExtension implements AroundAllTestsExtension {
    @Override
    public void beforeAllTests(ExtensionContext context) {
        System.out.println("BEFORE SUITE!");
    }

    @Override
    public void afterAllTests() {
        EmfContext.INSTANCE.storedEmf()
                .forEach(EntityManagerFactory::close);
    }
}
