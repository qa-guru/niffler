package niffler.data.jpa.query;

import niffler.data.DataSourceContext;
import niffler.data.model.UsersEntity;
import niffler.data.model.dao.UsersDAO;
import niffler.data.model.maper.UsersRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import static niffler.data.DataBase.USERDATA;

public class PostgresSpringJdbcUsers implements UsersDAO {

    private static final Logger LOG = LoggerFactory.getLogger(PostgresSpringJdbcUsers.class);
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceContext.INSTANCE.getDatatSource(USERDATA));

    @Override
    public int addUser(UsersEntity users) {
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
    public void updateUser(UsersEntity user) {
        jdbcTemplate.update("UPDATE users SET currency = ? WHERE username = ?", user.getCurrency(), user.getUsername());
    }

    @Override
    public void remove(UsersEntity user) {
        jdbcTemplate.update("DELETE from users WHERE id = ?", user.getId());
    }

    @Override
    public UsersEntity getByUsername(String username) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE username = ?",
                new UsersRowMapper(),
                username
        );
    }
}
