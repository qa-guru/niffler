package guru.qa.niffler.db;

import guru.qa.niffler.config.Config;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;

public enum DataSourceProvider {
  INSTANCE;

  private final Map<ServiceDB, DataSource> dataSources = new ConcurrentHashMap<>();

  public DataSource getDataSource(ServiceDB service) {
    return dataSources.computeIfAbsent(service, serviceDB -> {
      PGSimpleDataSource sds = new PGSimpleDataSource();
      sds.setURL(serviceDB.getJdbcUrl());
      sds.setUser(Config.getConfig().getDBLogin());
      sds.setPassword(Config.getConfig().getDBPassword());
      return sds;
    });
  }

}
