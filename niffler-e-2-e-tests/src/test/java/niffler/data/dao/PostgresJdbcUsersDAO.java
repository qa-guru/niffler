package niffler.data.dao;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import niffler.data.jdbc.DataSourceContext;
import niffler.data.entity.UsersEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static niffler.data.DataBase.USERDATA;

public class PostgresJdbcUsersDAO implements UsersDAO {

    private static final Logger LOG = LoggerFactory.getLogger(PostgresJdbcUsersDAO.class);
    private final DataSource ds = DataSourceContext.INSTANCE.getDatatSource(USERDATA);

    @Override
    public int addUser(UsersEntity users) {
        try (Connection con = ds.getConnection();
             Statement st = con.createStatement()) {
            String sql = "INSERT INTO users (username, currency, firstname, surname, photo) VALUES (" +
                    users.getUsername() +
                    users.getCurrency() +
                    users.getFirstname() +
                    users.getSurname() +
                    new String(users.getPhoto()) +
                    ");";
            return st.executeUpdate(sql);
        } catch (SQLException e) {
            LOG.error("Error while database operation", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateUser(UsersEntity user) {
        try (Connection con = ds.getConnection();
             Statement st = con.createStatement()) {
            String sql = "UPDATE users SET currency = '" + user.getCurrency() + "' WHERE username = '" + user.getUsername() + "';";
            st.executeUpdate(sql);
        } catch (SQLException e) {
            LOG.error("Error while database operation", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(UsersEntity user) {

    }

    @Override
    public UsersEntity getByUsername(String username) {
        try (Connection con = ds.getConnection();
             Statement st = con.createStatement()) {
            String sql = "SELECT * FROM users WHERE username = '" + username + "';";
            ResultSet resultSet = st.executeQuery(sql);
            if (resultSet.next()) {
                UsersEntity result = new UsersEntity();
                result.setId(UUID.fromString(resultSet.getString(1)));
                result.setUsername(resultSet.getString(2));
                result.setCurrency(resultSet.getString(3));
                result.setFirstname(resultSet.getString("firstname"));
                result.setSurname(resultSet.getString("surname"));
                result.setPhoto(resultSet.getBytes("photo"));
                return result;
            } else {
                String msg = "Can`t find user by username: " + username;
                LOG.error(msg);
                throw new RuntimeException(msg);
            }

        } catch (SQLException e) {
            LOG.error("Error while database operation", e);
            throw new RuntimeException(e);
        }
    }
}
