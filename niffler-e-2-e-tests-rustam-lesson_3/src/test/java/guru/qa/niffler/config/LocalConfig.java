package guru.qa.niffler.config;

public class LocalConfig implements Config {

  // Убрал паблик, чтобы был дефолт и видимость была только в пакете конфиг, а в тестах использовать было нельзя.
  static final LocalConfig config = new LocalConfig();
  // Приватный конструктор для того, чтобы нельзя было создавать новые объекты, а конфиг был синглтоном.
  private LocalConfig() {
  }

  @Override
  public String databaseHost() {
    return "localhost";
  }
}
