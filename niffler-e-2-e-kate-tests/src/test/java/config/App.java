
package config;

import org.aeonbits.owner.ConfigFactory;

public class App {
    public static final AppConfig CONFIG = ConfigFactory.create(AppConfig.class, System.getProperties());
    public static final String FRONTEND_URL = CONFIG.frontendUrl();
    public static final String SPEND_URI = CONFIG.spendUri();
}
