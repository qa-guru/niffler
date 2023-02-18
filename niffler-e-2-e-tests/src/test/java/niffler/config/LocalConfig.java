package niffler.config;

import com.codeborne.selenide.Configuration;

public class LocalConfig implements Config {

    static {
        Configuration.browserSize = "1980x1024";
    }

    @Override
    public String frontUrl() {
        return "http://127.0.0.1:3000/";
    }

    @Override
    public String userdataUrl() {
        return "http://127.0.0.1:8089/";
    }

    @Override
    public String currencyGrpcUrl() {
        return "http://127.0.0.1:8092/";
    }

    @Override
    public String spendUrl() {
        return "http://127.0.0.1:8093/";
    }

    @Override
    public String authUrl() {
        return "http://127.0.0.1:9000/";
    }
}
