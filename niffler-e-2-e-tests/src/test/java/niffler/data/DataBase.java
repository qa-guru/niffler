package niffler.data;

import niffler.config.Config;
import org.apache.commons.lang3.StringUtils;

public enum DataBase {
    USERDATA("jdbc:postgresql://%s/niffler-userdata"),
    AUTH("jdbc:postgresql://%s/niffler-auth"),
    SPEND("jdbc:postgresql://%s/niffler-spend"),
    CURRENCY("jdbc:postgresql://%s/niffler-currency");
    private final String url;

    DataBase(String url) {
        this.url = url;
    }

    private static final Config CFG = Config.getConfig();

    public String getUrl() {
        return String.format(url, CFG.databaseAddress());
    }

    public String getUrlForP6Spy() {
        return "jdbc:p6spy:" + StringUtils.substringAfter(getUrl(), "jdbc:");
    }
}
