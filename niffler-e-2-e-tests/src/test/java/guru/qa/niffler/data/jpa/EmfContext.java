package guru.qa.niffler.data.jpa;

import guru.qa.niffler.data.DataBase;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public enum EmfContext {
    INSTANCE;

    private final Map<DataBase, EntityManagerFactory> emfContext = new HashMap<>();

    public synchronized EntityManagerFactory getEmf(DataBase dataBase) {
        if (emfContext.get(dataBase) == null) {
            Map<String, String> settings = new HashMap<>();
            settings.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//            settings.put("hibernate.connection.driver_class", "org.postgresql.Driver");
            settings.put("hibernate.connection.driver_class", "com.p6spy.engine.spy.P6SpyDriver");
            settings.put("hibernate.connection.username", "postgres");
            settings.put("hibernate.connection.password", "secret");
            settings.put("hibernate.connection.url", dataBase.getUrlForP6Spy());
//            settings.put("hibernate.connection.url", dataBase.url);

            this.emfContext.put(
                    dataBase, new ThreadLocalEntityManagerFactory(
                            Persistence.createEntityManagerFactory(
                                    "niffler-persistence-unit-name",
                                    settings
                            )
                    )
            );
        }
        return emfContext.get(dataBase);
    }

    public Collection<EntityManagerFactory> storedEmf() {
        return emfContext.values();
    }
}
