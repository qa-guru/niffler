package guru.qa.niffler.db;

import guru.qa.niffler.config.Config;
import org.apache.commons.lang3.StringUtils;

public enum ServiceDB {

  NIFFLER_AUTH("jdbc:postgresql://%s:%d/niffler-auth"),
  NIFFLER_SPEND("jdbc:postgresql://%s:%d/niffler-spend"),
  NIFFLER_USERDATA("jdbc:postgresql://%s:%d/niffler-userdata"),
  NIFFLER_CURRENCY("jdbc:postgresql://%s:%d/niffler-currency");

  private final String jdbcUrl;

  ServiceDB(String jdbcUrl) {
    this.jdbcUrl = jdbcUrl;
  }

  public String getJdbcUrl() {
    return String.format(jdbcUrl,
        Config.getConfig().getDBHost(),
        Config.getConfig().getDBPort()
    );
  }

  public String p6SpyUrl() {
    String baseUrl = getJdbcUrl();
    return "jdbc:p6spy:" + StringUtils.substringAfter(baseUrl, "jdbc:");
  }
}
