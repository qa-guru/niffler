package niffler.config;

import org.aeonbits.owner.Config;

@org.aeonbits.owner.Config.Sources("classpath:config/app.properties")
interface AppConfig extends Config {

    @Key("application.spend.uri")
    String spendUri();

    @Key("application.userdata.uri")
    String userdataUri();

    @Key("application.frontend.url")
    String frontendUrl();

}
