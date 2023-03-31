package niffler.jupiter.extension;

import jakarta.persistence.EntityManagerFactory;
import niffler.data.jpa.EmfContext;
import niffler.jupiter.extension.AroundAllTestsExtension;
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
