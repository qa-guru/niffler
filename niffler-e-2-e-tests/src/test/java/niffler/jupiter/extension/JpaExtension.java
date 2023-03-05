package niffler.jupiter.extension;

import jakarta.persistence.EntityManagerFactory;
import niffler.data.jpa.EmfContext;

public class JpaExtension implements AroundAllTestsExtension {

    @Override
    public void afterAllTests() {
        EmfContext.INSTANCE.storedEmf()
                .forEach(EntityManagerFactory::close);
    }
}
