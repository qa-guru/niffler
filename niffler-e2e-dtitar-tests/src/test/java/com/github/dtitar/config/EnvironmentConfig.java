package com.github.dtitar.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "system:env"
})
public interface EnvironmentConfig extends Config {

    @DefaultValue("local")
    @Key("test.env")
    String env();
}
