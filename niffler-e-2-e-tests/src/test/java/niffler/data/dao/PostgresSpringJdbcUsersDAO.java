package niffler.data.dao;

import io.qameta.allure.Step;
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

    @Step("Add user to database using Spring-jdbc")
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

    @Step("Update user in database using Spring-jdbc")
    @Override
    public void updateUser(UsersEntity user) {
        jdbcTemplate.update("UPDATE users SET currency = ?,firstname = ?, surname = ?  WHERE username = ?",
                user.getCurrency().name(),
                user.getFirstname(),
                user.getSurname(),
                user.getUsername());
    }

    @Step("Remove user from database using Spring-jdbc")
    @Override
    public void remove(UsersEntity user) {
        jdbcTemplate.update("DELETE from users WHERE id = ?", user.getId());
    }

    @Step("Get user from database by username '{username}' using Spring-jdbc")
    @Override
    public UsersEntity getByUsername(String username) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE username = ?",
                new UsersRowMapper(),
                username
        );
    }
}
