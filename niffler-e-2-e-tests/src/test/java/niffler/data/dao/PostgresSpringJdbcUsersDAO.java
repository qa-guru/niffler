package niffler.data.dao;

import niffler.data.entity.UsersEntity;
import niffler.data.jdbc.DataSourceContext;
import niffler.data.spring_jdbc.UsersRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import static niffler.data.DataBase.USERDATA;

public class PostgresSpringJdbcUsersDAO implements UsersDAO {

    private static final Logger LOG = LoggerFactory.getLogger(PostgresSpringJdbcUsersDAO.class);
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
        jdbcTemplate.update("UPDATE users SET currency = ?,firstname = ?, surname = ?  WHERE username = ?",
                user.getCurrency().name(),
                user.getFirstname(),
                user.getSurname(),
                user.getUsername());
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
