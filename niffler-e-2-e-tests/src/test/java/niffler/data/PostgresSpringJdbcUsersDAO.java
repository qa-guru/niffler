package niffler.data;

import niffler.data.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static niffler.data.DataSourceContext.DataBase.USERDATA;

public class PostgresSpringJdbcUsersDAO implements UsersDAO {

    private static final Logger LOG = LoggerFactory.getLogger(PostgresSpringJdbcUsersDAO.class);
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceContext.INSTANCE.getDatatSource(USERDATA));

    @Override
    public int addUser(Users users) {
        return jdbcTemplate.update("INSERT INTO users " +
                        "(username, currency, firstname, surname, photo)" +
                        " VALUES (?, ?, ?, ?, ?)",
                users.getUsername(),
                users.getCurrency(),
                users.getFirstname(),
                users.getSurname(),
                new String(users.getPhoto())
        );
    }

    @Override
    public void updateUser(Users user) {
        jdbcTemplate.update("UPDATE users SET currency = ? WHERE username = ?", user.getCurrency(), user.getUsername());
    }

    @Override
    public void remove(Users user) {
        jdbcTemplate.update("DELETE from users WHERE id = ?", user.getId());
    }

    @Override
    public Users getByUsername(String username) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE username = ?",
                new UsersRowMapper(),
                username
        );
    }
}
