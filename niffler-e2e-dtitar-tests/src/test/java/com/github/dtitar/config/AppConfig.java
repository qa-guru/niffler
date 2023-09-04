package com.github.dtitar.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "classpath:config/${test.env}.properties"
})
public interface AppConfig extends Config {

    @Key("web.frontUrl")
    String frontUrl();

    @Key("api.gatewayUrl")
    String gatewayUrl();
    @Key("api.userdataUrl")
    String userdataUrl();
    @Key("api.spendUrl")
    String spendUrl();
    @Key("api.authUrl")
    String authUrl();

    @Key("grpc.currencyAddress")
    String currencyGrpcAddress();

    @Key("db.address")
    String databaseAddress();
    @Key("kafka.address")
    String kafkaAddress();
}
