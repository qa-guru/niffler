package guru.qa.niffler.config;

public class Local7Config implements Config {

  // Убрал паблик, чтобы был дефолт и видимость была только в пакете конфиг, а в тестах использовать было нельзя.
  static final Local7Config config = new Local7Config();
  // Приватный конструктор для того, чтобы нельзя было создавать новые объекты, а конфиг был синглтоном.
  private Local7Config() {
  }

  @Override
  public String databaseHost() {
    return "localhost";
  }

  @Override
  public String getSpendUrl() {
    return "http://127.0.0.1:8093";
  }

  @Override
  public String getCategoryUrl() {
    return "http://127.0.0.1:8093";
  }

  @Override
  public String getUserdataUrl() {
    return "http://127.0.0.1:8089/";
  }

  @Override
  public String getFrontUrl() {
    return "http://127.0.0.1:3000";
  }

  @Override
  public String getAuthUrl() {
    return "http://127.0.0.1:9000";
  }

  @Override
  public String getCurrencyGrpcAddress() {
    return "localhost";
  }

  @Override
  public int getCurrencyGrpcPort() {
    return 8092;
  }
}
