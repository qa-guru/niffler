package guru.qa.niffler.config;

import okhttp3.logging.HttpLoggingInterceptor;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface Config {

  String PROJECT_NAME = "niffler-ng";

  @Nonnull
  static Config getInstance() {
    if ("docker".equals(System.getProperty("test.env"))) {
      return DockerConfig.INSTANCE;
    } else if ("local".equals(System.getProperty("test.env"))) {
      return LocalConfig.INSTANCE;
    } else {
      throw new IllegalStateException();
    }
  }

  @Nonnull
  String authUrl();

  @Nonnull
  String frontUrl();

  @Nonnull
  String gatewayUrl();

  @Nonnull
  String userdataUrl();

  @Nonnull
  String currencyGrpcHost();

  default int currencyGrpcPort() {
    return 8092;
  }

  @Nonnull
  String spendUrl();

  @Nonnull
  String databaseAddress();

  @Nonnull
  String kafkaAddress();

  @Nonnull
  String allureDockerUrl();

  @Nonnull
  default List<String> kafkaTopics() {
    return List.of("users");
  }

  @Nonnull
  String screenshotBaseDir();

  @Nonnull
  HttpLoggingInterceptor.Level restLoggingLevel();
}
