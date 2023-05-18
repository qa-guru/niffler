package guru.qa.niffler.config;

public class DockerConfig implements Config {

  @Override
  public String getDBHost() {
    return "niffler-all-db";
  }

  @Override
  public String getDBLogin() {
    return "postgres";
  }

  @Override
  public String getDBPassword() {
    return "secret";
  }

  @Override
  public String getSpendUrl() {
    return "niffler-spend";
  }

  @Override
  public int getDBPort() {
    return 5432;
  }

  @Override
  public String getFrontUrl() {
    return "http://niffler-fronend:3000/";
  }

  @Override
  public String getAuthUrl() {
    return "http://niffler-auth:9000/";
  }
}
