package config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/app.properties")
public interface AppConfig extends Config {
    @Key("application.frontend.url")
    String frontendUrl();

    @Key("application.spend.uri")
    String spendUri();

    @Key("application.user.uri")
    String userUri();
}
