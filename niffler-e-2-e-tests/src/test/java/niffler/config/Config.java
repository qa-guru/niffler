package niffler.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Config {

    Logger LOG = LoggerFactory.getLogger(Config.class);
    String PROJECT_NAME = "niffler";

    static Config getConfig() {
        if ("docker".equals(System.getProperty("test.env"))) {
            return new DockerConfig();
        } else if ("local".equals(System.getProperty("test.env"))) {
            return new LocalConfig();
        } else {
            throw new IllegalStateException();
        }
    }

    String authUrl();

    String frontUrl();

    String gatewayUrl();

    String userdataUrl();

    String currencyGrpcAddress();

    int currencyGrpcPort();

    String spendUrl();

    String databaseAddress();
}
