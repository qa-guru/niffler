package niffler.data;

import org.apache.commons.lang3.StringUtils;

public enum DataBase {
    USERDATA("jdbc:postgresql://127.0.0.1:5432/niffler-userdata"),
    AUTH("jdbc:postgresql://127.0.0.1:5432/niffler-auth"),
    SPEND("jdbc:postgresql://127.0.0.1:5432/niffler-spend"),
    CURRENCY("jdbc:postgresql://127.0.0.1:5432/niffler-currency");
    private final String url;

    DataBase(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlForP6Spy() {
        return "jdbc:p6spy:" + StringUtils.substringAfter(url, "jdbc:");
    }
}
