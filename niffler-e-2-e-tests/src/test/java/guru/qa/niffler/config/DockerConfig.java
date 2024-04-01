package guru.qa.niffler.config;

import com.codeborne.selenide.Configuration;
import okhttp3.logging.HttpLoggingInterceptor;
import org.openqa.selenium.chrome.ChromeOptions;

import static guru.qa.niffler.utils.UrlUtils.isValidURL;

public class DockerConfig implements Config {

    static final DockerConfig INSTANCE = new DockerConfig();

    private DockerConfig() {
    }

    static {
        Configuration.browserSize = "1920x1200";
        Configuration.remote = "http://selenoid:4444/wd/hub";
        Configuration.timeout = 10000;
        Configuration.browser = "chrome";
        Configuration.browserVersion = "117.0";
        Configuration.browserCapabilities = new ChromeOptions().addArguments("--no-sandbox");
    }

    @Override
    public String frontUrl() {
        return "http://frontend.niffler.dc/";
    }

    @Override
    public String gatewayUrl() {
        return "http://gateway.niffler.dc:8090/";
    }

    @Override
    public String userdataUrl() {
        return "http://userdata.niffler.dc:8089/";
    }

    @Override
    public String currencyGrpcHost() {
        return "currency.niffler.dc";
    }

    @Override
    public String spendUrl() {
        return "http://spend.niffler.dc:8093/";
    }

    @Override
    public String authUrl() {
        return "http://auth.niffler.dc:9000/";
    }

    @Override
    public String databaseAddress() {
        return "niffler-all-db:5432";
    }

    @Override
    public String kafkaAddress() {
        return "kafka:9092";
    }

    @Override
    public String allureDockerUrl() {
        final String allureDockerApi = System.getenv("ALLURE_DOCKER_API");
        return isValidURL(allureDockerApi)
                ? allureDockerApi
                : "http://allure:5050/";
    }

    @Override
    public HttpLoggingInterceptor.Level restLoggingLevel() {
        return HttpLoggingInterceptor.Level.NONE;
    }
}
