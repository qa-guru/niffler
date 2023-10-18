package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.jpa.EntityManagerFactoryProvider;
import jakarta.persistence.EntityManagerFactory;

public class ExampleSuiteExtension implements SuiteExtension {

    @Override
    public void beforeSuite() {
        System.out.println("BEFORE SUITE!!!");
    }

    @Override
    public void afterSuite() {

        EntityManagerFactoryProvider.INSTANCE.storedEmf()
                .forEach(EntityManagerFactory::close);
        System.out.println("AFTER SUITE!!!");
    }
}
