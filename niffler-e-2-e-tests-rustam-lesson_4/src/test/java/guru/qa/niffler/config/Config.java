package guru.qa.niffler.config;

public interface Config {

  static Config getInstance() {
    if ("docker".equals(System.getProperty("test.env"))) {
      return DockerConfig.config;
    }
    return LocalConfig.config;
  }

  String databaseHost();

  default String databaseUser() {
    return "postgres";
  }

  default String databasePassword() {
    return "secret";
  }

  default int databasePort() {
    return 5434;
  }

}
