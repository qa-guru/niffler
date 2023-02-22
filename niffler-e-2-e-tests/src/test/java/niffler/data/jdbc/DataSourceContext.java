package niffler.data.jdbc;

import com.p6spy.engine.spy.P6DataSource;
import niffler.data.DataBase;
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
            ds.setURL(dataBase.getUrl());
            P6DataSource p6DataSource = new P6DataSource(ds);
            this.dsContext.put(dataBase, p6DataSource);
        }
        return dsContext.get(dataBase);
    }
}
