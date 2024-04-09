package guru.qa.niffler.data.jpa;

import guru.qa.niffler.data.DataBase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EmfContext {
    INSTANCE;

    private final Map<DataBase, EntityManagerFactory> emfContext = new ConcurrentHashMap<>();

    private EntityManagerFactory emf(DataBase dataBase) {
        return emfContext.computeIfAbsent(dataBase, key -> {
            Map<String, String> settings = new HashMap<>();
            settings.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//            settings.put("hibernate.connection.driver_class", "org.postgresql.Driver");
            settings.put("hibernate.connection.driver_class", "com.p6spy.engine.spy.P6SpyDriver");
            settings.put("hibernate.connection.username", "postgres");
            settings.put("hibernate.connection.password", "secret");
            settings.put("hibernate.connection.url", dataBase.getUrlForP6Spy());
//            settings.put("hibernate.connection.url", dataBase.url);
            return Persistence.createEntityManagerFactory(
                    "niffler-persistence-unit-name",
                    settings
            );
        });
    }

    public static EntityManager entityManager(DataBase dataBase) {
        return new TransactionalEntityManager(
                new ThreadSafeEntityManager(
                        INSTANCE.emf(dataBase).createEntityManager()
                )
        );
    }

    public static Collection<EntityManagerFactory> storedEmf() {
        return INSTANCE.emfContext.values();
    }
}
