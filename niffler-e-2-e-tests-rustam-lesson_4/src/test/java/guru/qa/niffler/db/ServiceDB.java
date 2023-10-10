package guru.qa.niffler.db;

import guru.qa.niffler.config.Config;

public enum ServiceDB {

  AUTH("jdbc:postgresql://%s:%d/niffler-auth"),
  USERDATA("jdbc:postgresql://%s:%d/niffler-userdata"),
  CURRENCY("jdbc:postgresql://%s:%d/niffler-currency"),
  SPEND("jdbc:postgresql://%s:%d/niffler-spend");

  private final String url;
  private static final Config cfg = Config.getInstance();

  ServiceDB(String url) {
    this.url = url;
  }

  public String getUrl() {
    return String.format(
        url,
        cfg.databaseHost(),
        cfg.databasePort()
    );
  }
}
