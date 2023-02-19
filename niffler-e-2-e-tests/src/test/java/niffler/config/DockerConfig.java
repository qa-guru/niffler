package niffler.config;

public class DockerConfig implements Config {

    @Override
    public String frontUrl() {
        return "http://127.0.0.1/";
    }

    @Override
    public String userdataUrl() {
        return "niffler-userdata";
    }

    @Override
    public String currencyGrpcUrl() {
        return "niffler-currency";
    }

    @Override
    public String spendUrl() {
        return "niffler-spend";
    }

    @Override
    public String authUrl() {
        return "niffler-auth";
    }
}
