package guru.qa.niffler.config;


import com.codeborne.selenide.Configuration;

public class LocalConfig implements Config {

    static final LocalConfig INSTANCE = new LocalConfig();

    static {
        Configuration.browser = "chrome";
        Configuration.browserVersion = "110.0";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10000;
    }

    private LocalConfig() {
    }

    @Override
    public String getDBHost() {
        return "localhost";
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
    public String getGatewayUrl() {
        return "http://127.0.0.1:8090";
    }

    @Override
    public String getUserdataUrl() {
        return "http://127.0.0.1:8089";
    }

    @Override
    public String getSpendUrl() {
        return "http://127.0.0.1:8093";
    }

    @Override
    public int getDBPort() {
        return 5432;
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
