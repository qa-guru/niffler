package niffler.data;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public enum DataSourceContext {
    INSTANCE;

    private Map<DataBase, DataSource> dsContext = new HashMap<>();

    public synchronized DataSource getDatatSource(DataBase dataBase) {
        if (dsContext.get(dataBase) == null) {
            PGSimpleDataSource ds = new PGSimpleDataSource();
            ds.setUser("postgres");
            ds.setPassword("secret");
            ds.setURL(dataBase.url);
            this.dsContext.put(dataBase, ds);
        }
        return dsContext.get(dataBase);
    }

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
}
