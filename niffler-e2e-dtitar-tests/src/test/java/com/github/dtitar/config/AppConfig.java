package com.github.dtitar.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "classpath:config/${test.env}.properties"
})
public interface AppConfig extends Config {

    String frontUrl();

    String gatewayUrl();

    String userdataUrl();

    String currencyGrpcAddress();

    String spendUrl();

    String authUrl();

    String databaseAddress();

    String kafkaAddress();
}
