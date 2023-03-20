package niffler.config;

import com.codeborne.selenide.Configuration;

public class DockerConfig implements Config {

    static {
        Configuration.browserSize = "1920x1200";
        Configuration.remote = "http://selenoid:4444/wd/hub";
        Configuration.timeout = 10000;
    }

    @Override
    public String frontUrl() {
        return "http://niffler-frontend/";
    }

    @Override
    public String gatewayUrl() {
        return "http://niffler-gateway:8090/";
    }

    @Override
    public String userdataUrl() {
        return "http://niffler-userdata:8089/";
    }

    @Override
    public String currencyGrpcAddress() {
        return "niffler-currency";
    }

    @Override
    public int currencyGrpcPort() {
        return 8092;
    }

    @Override
    public String spendUrl() {
        return "http://niffler-spend:8093/";
    }

    @Override
    public String authUrl() {
        return "http://niffler-auth:9000/";
    }

    @Override
    public String databaseAddress() {
        return "niffler-all-db:5432";
    }
}
