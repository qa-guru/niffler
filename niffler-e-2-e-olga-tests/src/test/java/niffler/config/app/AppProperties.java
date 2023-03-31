package niffler.config.app;

import org.aeonbits.owner.ConfigFactory;

public final class AppProperties {

    private static final AppConfig CONFIG = ConfigFactory.create(AppConfig.class);

    public static final String SPEND_APP_URI = CONFIG.spendUri(),
            USERDATA_APP_URI = CONFIG.userdataUri(),
            FRONTEND_APP_URL = CONFIG.frontendUrl();

    public static final String USER = CONFIG.user(),
            PASSWORD = CONFIG.password();

    public static final String USERDATA_URL = CONFIG.userdataUrl(),
            AUTH_URL = CONFIG.authUrl(),
            SPEND_URL = CONFIG.spendUrl(),
            CURRENCY_URL = CONFIG.currencyUrl();

}
