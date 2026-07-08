package guru.qa.niffler.config;

import com.codeborne.selenide.Configuration;
import okhttp3.logging.HttpLoggingInterceptor;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LocalConfig implements Config {

  static final LocalConfig INSTANCE = new LocalConfig();

  private LocalConfig() {
  }

  static {
    Configuration.browserSize = "1980x1024";
    Configuration.browser = "chrome";
    Configuration.pageLoadStrategy = "eager";
    Configuration.browserCapabilities = new ChromeOptions()
        .addArguments("--no-sandbox")
        .addArguments("--lang=en")
        .setExperimentalOption("prefs", Map.of(
            "intl.accept_languages", "en",
            "intl.selected_languages", "en"
        ));
  }

  @Nonnull
  @Override
  public String frontUrl() {
    return "http://localhost:3000/";
  }

  @Nonnull
  @Override
  public String gatewayUrl() {
    return "http://localhost:8090/";
  }

  @Nonnull
  @Override
  public String userdataUrl() {
    return "http://localhost:8089/";
  }

  @Nonnull
  @Override
  public String currencyGrpcHost() {
    return "localhost";
  }

  @Nonnull
  @Override
  public String spendUrl() {
    return "http://localhost:8093/";
  }

  @Nonnull
  @Override
  public String authUrl() {
    return "http://localhost:9000/";
  }

  @Nonnull
  @Override
  public String databaseAddress() {
    return "localhost:5432";
  }

  @Nonnull
  @Override
  public String kafkaAddress() {
    return "localhost:9092";
  }

  @Nonnull
  @Override
  public String allureDockerUrl() {
    return "http://localhost:5050/";
  }

  @Nonnull
  @Override
  public String screenshotBaseDir() {
    return "screenshots/local/";
  }

  @Nonnull
  @Override
  public HttpLoggingInterceptor.Level restLoggingLevel() {
    return HttpLoggingInterceptor.Level.BODY;
  }
}
