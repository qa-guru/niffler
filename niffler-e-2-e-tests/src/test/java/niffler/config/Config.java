package niffler.config;

public interface Config {

    String PROJECT_NAME = "niffler";

    static Config getConfig() {
        if ("docker".equals(System.getProperty("test.env"))) {
            return new DockerConfig();
        } else if ("local".equals(System.getProperty("test.env"))) {
            return new LocalConfig();
        } else throw new IllegalStateException();
    }

    String authUrl();

    String frontUrl();

    String userdataUrl();

    String currencyGrpcUrl();

    String spendUrl();
}