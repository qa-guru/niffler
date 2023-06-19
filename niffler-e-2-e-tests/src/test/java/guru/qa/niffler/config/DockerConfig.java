package guru.qa.niffler.config;

import com.codeborne.selenide.Configuration;

public class DockerConfig implements Config {

    static final DockerConfig INSTANCE = new DockerConfig();

    static {
        Configuration.browser = "chrome";
        Configuration.browserVersion = "110.0";
        Configuration.remote = "http://selenoid:4444/wd/hub";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10000;
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
    public int getDBPort() {
        return 5432;
    }

    @Override
    public String getFrontUrl() {
        return "http://frontend.niffler.dc";
    }

    @Override
    public String getAuthUrl() {
        return "http://auth.niffler.dc:9000";
    }

    @Override
    public String getGatewayUrl() {
        return "http://gateway.niffler.dc:8090";
    }

    @Override
    public String getUserdataUrl() {
        return "http://userdata.niffler.dc:8089";
    }

    @Override
    public String getSpendUrl() {
        return "http://spend.niffler.dc:8093";
    }

    @Override
    public String getCurrencyGrpcAddress() {
        return "currency.niffler.dc";
    }

    @Override
    public int getCurrencyGrpcPort() {
        return 8092;
    }
}
