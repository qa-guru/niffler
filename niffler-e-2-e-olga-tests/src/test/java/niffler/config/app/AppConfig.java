package niffler.config.app;

import org.aeonbits.owner.Config;

@org.aeonbits.owner.Config.Sources("classpath:config/app.properties")
interface AppConfig extends Config {

    @Key("application.spend.uri")
    String spendUri();

    @Key("application.userdata.uri")
    String userdataUri();

    @Key("application.frontend.url")
    String frontendUrl();

    @Config.Key("database.user")
    String user();

    @Config.Key("database.password")
    String password();

    @Config.Key("userdata.database.url")
    String userdataUrl();

    @Config.Key("auth.database.url")
    String authUrl();

    @Config.Key("spend.database.url")
    String spendUrl();

    @Config.Key("currency.database.url")
    String currencyUrl();

}
