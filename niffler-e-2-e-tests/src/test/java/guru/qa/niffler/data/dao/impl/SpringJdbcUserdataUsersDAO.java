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
                "INSERT INTO \"user\" (username, currency) VALUES (?, ?)",
                user.getUsername(),
                CurrencyValues.RUB.name()
        );
    }

    @Step("Update user in userdata database using Spring-jdbc")
    @Override
    public UserEntity updateUser(UserEntity user) {
        userdataTransactionTpl.execute(status -> {
            userdataJdbcTpl.update("UPDATE \"user\" SET " +
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

            userdataJdbcTpl.batchUpdate("INSERT INTO friendship (requester_id, addressee_id, status) " +
                    "VALUES (?, ?, ?) " +
                    "ON CONFLICT (requester_id, addressee_id) " +
                    "DO UPDATE SET status = ?", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setObject(1, user.getId());
                    ps.setObject(2, user.getFriendshipRequests().get(i).getAddressee().getId());
                    ps.setString(3, user.getFriendshipRequests().get(i).getStatus().name());
                    ps.setString(4, user.getFriendshipRequests().get(i).getStatus().name());
                }

                @Override
                public int getBatchSize() {
                    return user.getFriendshipRequests().size();
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
            userdataJdbcTpl.update("DELETE FROM friendship WHERE requester_id = ?", user.getId());
            userdataJdbcTpl.update("DELETE FROM friendship WHERE addressee_id = ?", user.getId());
            userdataJdbcTpl.update("DELETE FROM \"user\" WHERE id = ?", user.getId());
            return null;
        });
    }

    @Step("Find user in userdata database by username '{username}' using Spring-jdbc")
    @Override
    public Optional<UserEntity> findUserByUsername(String username) {
        UserEntity user = userdataJdbcTpl.queryForObject(
                "SELECT * FROM \"user\" WHERE username = ? ",
                UserEntityRowMapper.instance,
                username
        );
        return user == null ? Optional.empty() : Optional.of(user);
    }
}
