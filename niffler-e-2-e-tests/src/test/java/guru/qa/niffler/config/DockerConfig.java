package guru.qa.niffler.config;

import com.codeborne.selenide.Configuration;

public class DockerConfig implements Config {

  static final DockerConfig INSTANCE = new DockerConfig();

  static {
    Configuration.browser = "chrome";
    Configuration.browserVersion = "110.0";
    Configuration.remote = "http://selenoid:4444/wd/hub";
    Configuration.browserSize = "1920x1080";
  }
  private DockerConfig() {
  }

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
    return "http://niffler-spend:8093";
  }

  @Override
  public int getDBPort() {
    return 5432;
  }

  @Override
  public String getFrontUrl() {
    return "http://niffler-frontend";
  }

  @Override
  public String getAuthUrl() {
    return "http://niffler-auth:9000";
  }

  @Override
  public String getCurrencyGrpcAddress() {
    return "http://niffler-currency:8091";
  }

  @Override
  public int getCurrencyGrpcPort() {
    return 8092;
  }
}
