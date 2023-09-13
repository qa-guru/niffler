package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.UserdataUsersDAO;
import guru.qa.niffler.data.entity.ud.CurrencyValues;
import guru.qa.niffler.data.entity.ud.UserEntity;
import guru.qa.niffler.data.jdbc.DataSourceContext;
import guru.qa.niffler.data.spring_jdbc.UserEntityRowMapper;
import io.qameta.allure.Step;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import static guru.qa.niffler.data.DataBase.USERDATA;

public class SpringJdbcUserdataUsersDAO implements UserdataUsersDAO {

    private final TransactionTemplate userdataTransactionTpl;
    private final JdbcTemplate userdataJdbcTpl;

    public SpringJdbcUserdataUsersDAO() {
        JdbcTransactionManager userdataTm = new JdbcTransactionManager(
                DataSourceContext.INSTANCE.getDatatSource(USERDATA));

        this.userdataTransactionTpl = new TransactionTemplate(userdataTm);
        this.userdataJdbcTpl = new JdbcTemplate(userdataTm.getDataSource());
    }

    @Step("Add user to userdata database using Spring-jdbc")
    @Override
    public void createUser(UserEntity user) {
        userdataJdbcTpl.update(
                "INSERT INTO users (username, currency) VALUES (?, ?)",
                user.getUsername(),
                CurrencyValues.RUB.name()
        );
    }

    @Step("Update user in userdata database using Spring-jdbc")
    @Override
    public UserEntity updateUser(UserEntity user) {
        userdataTransactionTpl.execute(status -> {
            userdataJdbcTpl.update("UPDATE users SET " +
                            "currency = ?, " +
                            "firstname = ?, " +
                            "surname = ?, " +
                            "photo = ? " +
                            "WHERE id = ? ",
                    user.getCurrency().name(),
                    user.getFirstname(),
                    user.getSurname(),
                    user.getPhoto(),
                    user.getId());

            userdataJdbcTpl.batchUpdate("INSERT INTO friends (user_id, friend_id, pending) " +
                    "VALUES (?, ?, ?) " +
                    "ON CONFLICT (user_id, friend_id) " +
                    "DO UPDATE SET pending = ?", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setObject(1, user.getId());
                    ps.setObject(2, user.getFriends().get(i).getFriend().getId());
                    ps.setBoolean(3, user.getFriends().get(i).isPending());
                    ps.setBoolean(4, user.getFriends().get(i).isPending());
                }

                @Override
                public int getBatchSize() {
                    return user.getFriends().size();
                }
            });
            return null;
        });
        return findUserByUsername(user.getUsername()).get();
    }

    @Step("Remove user from userdata database using Spring-jdbc")
    @Override
    public void deleteUser(UserEntity user) {
        userdataTransactionTpl.execute(status -> {
            userdataJdbcTpl.update("DELETE FROM friends WHERE user_id = ?", user.getId());
            userdataJdbcTpl.update("DELETE FROM friends WHERE friend_id = ?", user.getId());
            userdataJdbcTpl.update("DELETE FROM users WHERE id = ?", user.getId());
            return null;
        });
    }

    @Step("Find user in userdata database by username '{username}' using Spring-jdbc")
    @Override
    public Optional<UserEntity> findUserByUsername(String username) {
        UserEntity user = userdataJdbcTpl.queryForObject(
                "SELECT * FROM users WHERE username = ? ",
                UserEntityRowMapper.instance,
                username
        );
        return user == null ? Optional.empty() : Optional.of(user);
    }
}
