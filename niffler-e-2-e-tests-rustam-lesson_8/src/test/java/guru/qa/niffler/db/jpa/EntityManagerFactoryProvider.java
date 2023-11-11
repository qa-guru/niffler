package guru.qa.niffler.db.jpa;

import com.p6spy.engine.spy.P6SpyDriver;
import com.p6spy.engine.spy.P6SpyFactory;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.db.ServiceDB;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EntityManagerFactoryProvider {
  INSTANCE;

  private static final Config cfg = Config.getInstance();

  private final Map<ServiceDB, EntityManagerFactory> entityManagerFactory = new ConcurrentHashMap<>();

  // Содержит информацию о том, как подключаться к базе данных
  public EntityManagerFactory getEntityManagerFactory(ServiceDB db) {

    return entityManagerFactory.computeIfAbsent(db, key -> {

      Map<String, Object> props = new HashMap<>();
//      props.put("hibernate.connection.url", key.getUrl());
      props.put("hibernate.connection.url", key.getSpyUrl());
      props.put("hibernate.connection.user", cfg.databaseUser());
      props.put("hibernate.connection.password", cfg.databasePassword());
//      props.put("hibernate.connection.driver_class", "org.postgresql.Driver");
      props.put("hibernate.connection.driver_class", "com.p6spy.engine.spy.P6SpyDriver");
      props.put("hibernate.connection.dialect", "org.hibernate.dialect.PostgreSQLDialect");

      EntityManagerFactory entityManagerFactory =
          new ThreadLocalEntityManagerFactory(
              Persistence.createEntityManagerFactory(
                  "rustam-niffler-persistence-unit-name", props)
          );
      return entityManagerFactory;
    });

  }

  public Collection<EntityManagerFactory> storedEmf() {
    return entityManagerFactory.values();
  }
}
