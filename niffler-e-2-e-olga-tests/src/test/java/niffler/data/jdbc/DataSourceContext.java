package niffler.data.jdbc;

import niffler.data.DataBase;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public enum DataSourceContext {
    INSTANCE;

    private Map<DataBase, DataSource> dsContext = new HashMap<>();

    public synchronized DataSource getDataSource(DataBase dataBase) {
        if (dsContext.get(dataBase) == null) {
            PGSimpleDataSource dataSource = new PGSimpleDataSource();
            dataSource.setUser(dataBase.getUser());
            dataSource.setPassword(dataBase.getPassword());
            dataSource.setURL(dataBase.getUrl());
            this.dsContext.put(dataBase, dataSource);
        }
        return dsContext.get(dataBase);
    }
}
