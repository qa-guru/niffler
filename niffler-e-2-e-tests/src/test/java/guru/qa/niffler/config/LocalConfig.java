package guru.qa.niffler.config;

import com.codeborne.selenide.Configuration;
import okhttp3.logging.HttpLoggingInterceptor;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Map;

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

  @Override
  public String frontUrl() {
    return "https://niffler-stage.qa.guru/";
  }

  @Override
  public String gatewayUrl() {
    return "https://api.niffler-stage.qa.guru/";
  }

  @Override
  public String userdataUrl() {
    return "http://localhost:8089/";
  }

  @Override
  public String currencyGrpcHost() {
    return "localhost";
  }

  @Override
  public String spendUrl() {
    return "http://localhost:8093/";
  }

  @Override
  public String authUrl() {
    return "https://auth.niffler-stage.qa.guru/";
  }

  @Override
  public String databaseAddress() {
    return "localhost:5432";
  }

  @Override
  public String kafkaAddress() {
    return "localhost:9092";
  }

  @Override
  public String allureDockerUrl() {
    return "http://localhost:5050/";
  }

  @Override
  public String screenshotBaseDir() {
    return "screenshots/local/";
  }

  @Override
  public HttpLoggingInterceptor.Level restLoggingLevel() {
    return HttpLoggingInterceptor.Level.BODY;
  }
}
