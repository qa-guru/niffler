package niffler.data;

public enum DataBase {
    USERDATA("jdbc:postgresql://127.0.0.1:5432/niffler-userdata"),
    AUTH("jdbc:postgresql://127.0.0.1:5432/niffler-auth"),
    SPEND("jdbc:postgresql://127.0.0.1:5432/niffler-spend"),
    CURRENCY("jdbc:postgresql://127.0.0.1:5432/niffler-currency");
    public final String url;

    DataBase(String url) {
        this.url = url;
    }
}
