package com.github.dtitar.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:config/${test.env}.properties"
})
public interface DriverConfig extends Config {
    @Key("driver.browserSize")
    String browserSize();
    @Key("driver.remoteUrl")
    String remoteUrl();
}
