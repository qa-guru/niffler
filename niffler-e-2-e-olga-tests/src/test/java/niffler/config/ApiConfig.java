package niffler.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/app.properties")
interface AppConfig extends Config {

    @Key("application.spend.uri")
    String spendUri();

    @Key("application.frontend.url")
    String frontendUrl();

}
