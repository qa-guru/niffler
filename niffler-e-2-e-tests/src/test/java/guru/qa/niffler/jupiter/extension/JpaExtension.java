package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.jpa.EmfContext;
import jakarta.persistence.EntityManagerFactory;

public class JpaExtension implements AroundAllTestsExtension {

    @Override
    public void afterAllTests() {
        EmfContext.INSTANCE.storedEmf()
                .forEach(EntityManagerFactory::close);
    }
}
