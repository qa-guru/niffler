package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.sjdbc.AuthUserEntityRowMapper;
import guru.qa.niffler.data.sjdbc.AuthorityEntityRowMapper;
import guru.qa.niffler.data.sjdbc.UserEntityRowMapper;
import io.qameta.allure.Step;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.DataBase.AUTH;
import static guru.qa.niffler.data.DataBase.USERDATA;
import static guru.qa.niffler.data.jdbc.DataSourceContext.dataSource;

public class UserRepositorySpringJdbc implements UserRepository {

    private final TransactionTemplate authTxTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    dataSource(AUTH)
            )
    );

    private final TransactionTemplate userdataTxTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    dataSource(USERDATA)
            )
    );

    private final JdbcTemplate authJdbcTemplate = new JdbcTemplate(dataSource(AUTH));
    private final JdbcTemplate userdataJdbcTemplate = new JdbcTemplate(dataSource(USERDATA));

    @Step("Create user in auth db using Spring-jdbc")
    @Override
    public AuthUserEntity createInAuth(AuthUserEntity user) {
        return authTxTemplate.execute(status -> {
            KeyHolder kh = new GeneratedKeyHolder();

            authJdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement("INSERT INTO \"user\" " +
                                "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                                "VALUES (?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                ps.setBoolean(3, user.getEnabled());
                ps.setBoolean(4, user.getAccountNonExpired());
                ps.setBoolean(5, user.getAccountNonLocked());
                ps.setBoolean(6, user.getCredentialsNonExpired());
                return ps;
            }, kh);
            final UUID generatedUserId = (UUID) kh.getKeys().get("id");
            authJdbcTemplate.batchUpdate("INSERT INTO  \"authority\" (user_id, authority) VALUES (?, ?)", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setObject(1, generatedUserId);
                    ps.setString(2, user.getAuthorities().get(i).getAuthority().name());
                }

                @Override
                public int getBatchSize() {
                    return user.getAuthorities().size();
                }
            });
            user.setId(generatedUserId);
            return user;
        });
    }

    @Step("Find user in auth db by id: '{id}' using Spring-jdbc")
    @Override
    public Optional<AuthUserEntity> findByIdInAuth(UUID id) {
        AuthUserEntity authUser = authJdbcTemplate.queryForObject(
                "SELECT * FROM \"user\" WHERE id = ?",
                AuthUserEntityRowMapper.instance, id
        );
        if (authUser == null) {
            return Optional.empty();
        }

        List<AuthorityEntity> authorityEntityList = authJdbcTemplate.query(
                "SELECT * FROM \"authority\" WHERE user_id = ?",
                AuthorityEntityRowMapper.instance, id
        );
        authUser.addAuthorities(authorityEntityList);
        return Optional.of(authUser);
    }

    @Step("Create user in userdata db using Spring-jdbc")
    @Override
    public UserEntity createInUserdata(UserEntity user) {
        KeyHolder kh = new GeneratedKeyHolder();

        userdataJdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO \"user\" (username, currency) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            return ps;
        }, kh);
        final UUID generatedUserId = (UUID) kh.getKeys().get("id");
        user.setId(generatedUserId);
        return user;
    }

    @Step("Find user in userdata db by id: '{id}' using Spring-jdbc")
    @Override
    public Optional<UserEntity> findByIdInUserdata(UUID id) {
        return Optional.ofNullable(
                userdataJdbcTemplate.queryForObject(
                        "SELECT * FROM \"user\" WHERE id = ? ",
                        UserEntityRowMapper.instance,
                        id
                )
        );
    }

    @Step("Update user in auth db using Spring-jdbc")
    @Override
    public void updateInAuth(AuthUserEntity user) {
        authTxTemplate.execute(status -> {
            authJdbcTemplate.update("UPDATE \"user\" SET " +
                            "password = ?, " +
                            "enabled = ?, " +
                            "account_non_expired = ?, " +
                            "account_non_locked = ?, " +
                            "credentials_non_expired = ? " +
                            "WHERE id = ? ",
                    user.getPassword(),
                    user.getEnabled(),
                    user.getAccountNonExpired(),
                    user.getAccountNonLocked(),
                    user.getCredentialsNonExpired(),
                    user.getId()
            );
            authJdbcTemplate.update("DELETE FROM authority WHERE user_id = ?", user.getId());
            authJdbcTemplate.batchUpdate(
                    "INSERT INTO authority (user_id, authority) VALUES (?, ?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setObject(1, user.getId());
                            ps.setString(2, user.getAuthorities().get(i).getAuthority().name());
                        }

                        @Override
                        public int getBatchSize() {
                            return user.getAuthorities().size();
                        }
                    });
            return null;
        });
    }

    @Step("Update user in userdata db using Spring-jdbc")
    @Override
    public void updateInUserdata(UserEntity user) {
        userdataTxTemplate.execute(status -> {
            userdataJdbcTemplate.update("UPDATE \"user\" SET " +
                            "currency = ?, " +
                            "firstname = ?, " +
                            "surname = ?, " +
                            "photo = ?, " +
                            "photo_small = ? " +
                            "WHERE id = ? ",
                    user.getCurrency().name(),
                    user.getFirstname(),
                    user.getSurname(),
                    user.getPhoto(),
                    user.getPhotoSmall(),
                    user.getId());

            userdataJdbcTemplate.batchUpdate("INSERT INTO friendship (requester_id, addressee_id, status) " +
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
    }

    @Step("Remove user from auth db using Spring-jdbc")
    @Override
    public void deleteInAuth(AuthUserEntity user) {
        authTxTemplate.execute(status -> {
            authJdbcTemplate.update("DELETE FROM authority WHERE user_id = ?", user.getId());
            authJdbcTemplate.update("DELETE FROM \"user\" WHERE id = ?", user.getId());
            return null;
        });
    }

    @Step("Remove user from userdata db using Spring-jdbc")
    @Override
    public void deleteInUserdata(UserEntity user) {
        userdataTxTemplate.execute(status -> {
            userdataJdbcTemplate.update("DELETE FROM friendship WHERE requester_id = ?", user.getId());
            userdataJdbcTemplate.update("DELETE FROM friendship WHERE addressee_id = ?", user.getId());
            userdataJdbcTemplate.update("DELETE FROM \"user\" WHERE id = ?", user.getId());
            return null;
        });
    }
}
