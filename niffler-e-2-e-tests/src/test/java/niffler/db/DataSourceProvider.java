package niffler.db;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import javax.sql.DataSource;
import niffler.config.Config;
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
