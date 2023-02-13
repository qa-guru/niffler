package niffler.config;

import org.aeonbits.owner.ConfigFactory;

public final class AppProperties {

    private static final AppConfig CONFIG = ConfigFactory.create(AppConfig.class);

    public static final String SPEND_APP_URI = CONFIG.spendUri(),
            USERDATA_APP_URI = CONFIG.userdataUri(),
            FRONTEND_APP_URL = CONFIG.frontendUrl();

}
