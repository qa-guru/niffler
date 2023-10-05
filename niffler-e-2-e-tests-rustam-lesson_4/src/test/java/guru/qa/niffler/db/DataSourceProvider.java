package guru.qa.niffler.db;

import guru.qa.niffler.config.Config;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;

public enum DataSourceProvider {
  INSTANCE;

  private static final Config cfg = Config.getInstance();

//  // Плохой код
//  // Ленивая инициализация
//  private PGSimpleDataSource authDs = null;
//  private PGSimpleDataSource userdataDs = null;
//  private PGSimpleDataSource currencyDs = null;
//  private PGSimpleDataSource spendDs = null;
//
//  // Содержит информацию о том, как подключаться к базе данных
//  public DataSource getDataSource(ServiceDB db) {
//    switch (db) {
//      case AUTH -> {
//        if (authDs == null) {
//          authDs = new PGSimpleDataSource();
//          authDs.setUrl(db.getUrl());
//          authDs.setUser(cfg.databaseUser());
//          authDs.setPassword(cfg.databasePassword());
//        }
//        return authDs;
//      }
//      case USERDATA -> {
//        if (userdataDs == null) {
//          userdataDs = new PGSimpleDataSource();
//          userdataDs.setUrl(db.getUrl());
//          userdataDs.setUser(cfg.databaseUser());
//          userdataDs.setPassword(cfg.databasePassword());
//        }
//        return userdataDs;
//      }
//      case CURRENCY -> {
//        if (currencyDs == null) {
//          currencyDs = new PGSimpleDataSource();
//          currencyDs.setUrl(db.getUrl());
//          currencyDs.setUser(cfg.databaseUser());
//          currencyDs.setPassword(cfg.databasePassword());
//        }
//        return currencyDs;
//      }
//      case SPEND -> {
//        if (spendDs == null) {
//          spendDs = new PGSimpleDataSource();
//          spendDs.setUrl(db.getUrl());
//          spendDs.setUser(cfg.databaseUser());
//          spendDs.setPassword(cfg.databasePassword());
//        }
//        return spendDs;
//      }
//    }
//    return null;
//  }

  // Хороший код
  // Ленивая инициализация
  private final Map<ServiceDB, DataSource> dataSourceStore = new ConcurrentHashMap<>();

  // Содержит информацию о том, как подключаться к базе данных
  public DataSource getDataSource(ServiceDB db) {

    return dataSourceStore.computeIfAbsent(db, key -> {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setUrl(key.getUrl());
        ds.setUser(cfg.databaseUser());
        ds.setPassword(cfg.databasePassword());
        return ds;
    });

  }
}
