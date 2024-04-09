package guru.qa.niffler.data.jdbc;

import com.p6spy.engine.spy.P6DataSource;
import guru.qa.niffler.data.DataBase;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public enum DataSourceContext {
    INSTANCE;

    private final Map<DataBase, DataSource> dsContext = new HashMap<>();

    private synchronized DataSource getDataSource(DataBase dataBase) {
        if (dsContext.get(dataBase) == null) {
            PGSimpleDataSource ds = new PGSimpleDataSource();
            ds.setUser("postgres");
            ds.setPassword("secret");
            ds.setURL(dataBase.getUrl());
            P6DataSource p6DataSource = new P6DataSource(ds);
            this.dsContext.put(dataBase, p6DataSource);
        }
        return dsContext.get(dataBase);
    }

    public static DataSource dataSource(DataBase dataBase) {
        return INSTANCE.getDataSource(dataBase);
    }
}
