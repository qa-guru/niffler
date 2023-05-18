package guru.qa.niffler.config;

public interface Config {

  static Config getConfig() {
    if ("docker".equals(System.getProperty("env"))) {
      return new DockerConfig();
    }
    return new LocalConfig();
  }

  String getDBHost();

  String getDBLogin();

  String getDBPassword();

  String getSpendUrl();

  int getDBPort();

  String getFrontUrl();

  String getAuthUrl();
}
