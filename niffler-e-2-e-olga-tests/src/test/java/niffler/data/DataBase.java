package niffler.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import niffler.config.app.AppProperties;

@Getter
@AllArgsConstructor
public enum DataBase {
    USERDATA(AppProperties.USERDATA_URL),
    AUTH(AppProperties.AUTH_URL),
    SPEND(AppProperties.SPEND_URL),
    CURRENCY(AppProperties.CURRENCY_URL);
    private final String url;
    private final String user = AppProperties.USER,
            password = AppProperties.PASSWORD;
}
