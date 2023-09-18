package guru.qa.niffler.config;

import java.util.List;

public interface Config {

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
