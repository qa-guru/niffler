package guru.qa.niffler.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface Config {

    Logger LOG = LoggerFactory.getLogger(Config.class);
    String PROJECT_NAME = "niffler";

    static Config getConfig() {
        if ("docker".equals(System.getProperty("test.env"))) {
            return DockerConfig.INSTANCE;
        } else if ("local".equals(System.getProperty("test.env"))) {
            return LocalConfig.INSTANCE;
        } else {
            throw new IllegalStateException();
        }
    }

    String authUrl();

    String frontUrl();

    String gatewayUrl();

    String userdataUrl();

    String currencyGrpcAddress();

    default int currencyGrpcPort() {
        return 8092;
    }

    String spendUrl();

    String databaseAddress();

    String kafkaAddress();

    default List<String> kafkaTopics() {
        return List.of("users");
    }
}
