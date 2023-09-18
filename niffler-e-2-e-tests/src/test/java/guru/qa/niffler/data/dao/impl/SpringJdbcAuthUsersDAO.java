package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthUsersDAO;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.jdbc.DataSourceContext;
import guru.qa.niffler.data.spring_jdbc.AuthUserEntityRowMapper;
import guru.qa.niffler.data.spring_jdbc.AuthorityEntityRowMapper;
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

public class SpringJdbcAuthUsersDAO implements AuthUsersDAO {

    private final TransactionTemplate authTransactionTpl;
    private final JdbcTemplate authJdbcTpl;

    public SpringJdbcAuthUsersDAO() {
        JdbcTransactionManager authTm = new JdbcTransactionManager(
                DataSourceContext.INSTANCE.getDatatSource(AUTH));

        this.authTransactionTpl = new TransactionTemplate(authTm);
        this.authJdbcTpl = new JdbcTemplate(authTm.getDataSource());
    }

    @Step("Add user to auth database using Spring-jdbc")
    @Override
    public void createUser(AuthUserEntity user) {
        authTransactionTpl.execute(status -> {
            KeyHolder kh = new GeneratedKeyHolder();

            authJdbcTpl.update(con -> {
                PreparedStatement ps = con.prepareStatement("INSERT INTO users " +
                                "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                                "VALUES (?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.getUsername());
                ps.setString(2, pe.encode(user.getPassword()));
                ps.setBoolean(3, user.getEnabled());
                ps.setBoolean(4, user.getAccountNonExpired());
                ps.setBoolean(5, user.getAccountNonLocked());
                ps.setBoolean(6, user.getCredentialsNonExpired());
                return ps;
            }, kh);
            final UUID generatedUserId = (UUID) kh.getKeyList().get(0).get("id");
            authJdbcTpl.batchUpdate("INSERT INTO authorities (user_id, authority) VALUES (?, ?)", new BatchPreparedStatementSetter() {
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
            return null;
        });
    }

    @Step("Update user in auth database using Spring-jdbc")
    @Override
    public AuthUserEntity updateUser(AuthUserEntity user) {
        authTransactionTpl.execute(status -> {
            authJdbcTpl.update("UPDATE users SET " +
                            "password = ?, " +
                            "enabled = ?, " +
                            "account_non_expired = ?, " +
                            "account_non_locked = ?, " +
                            "credentials_non_expired = ? " +
                            "WHERE id = ? ",
                    pe.encode(user.getPassword()),
                    user.getEnabled(),
                    user.getAccountNonExpired(),
                    user.getAccountNonLocked(),
                    user.getCredentialsNonExpired(),
                    user.getId()
            );
            authJdbcTpl.update("DELETE FROM authorities WHERE user_id = ?", user.getId());
            authJdbcTpl.batchUpdate("INSERT INTO authorities (user_id, authority) VALUES (?, ?)", new BatchPreparedStatementSetter() {
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
        return findUserById(user.getId()).get();
    }

    @Step("Remove user from auth database using Spring-jdbc")
    @Override
    public void deleteUser(AuthUserEntity user) {
        authTransactionTpl.execute(status -> {
            authJdbcTpl.update("DELETE FROM authorities WHERE user_id = ?", user.getId());
            authJdbcTpl.update("DELETE FROM users WHERE id = ?", user.getId());
            return null;
        });
    }

    @Step("Find user in auth database by user id '{userId}' using Spring-jdbc")
    @Override
    public Optional<AuthUserEntity> findUserById(UUID userId) {
        AuthUserEntity authUser = authJdbcTpl.queryForObject(
                "SELECT * FROM users WHERE id = ?",
                AuthUserEntityRowMapper.instance, userId
        );
        if (authUser == null) {
            return Optional.empty();
        }

        List<AuthorityEntity> authorityEntityList = authJdbcTpl.query(
                "SELECT * FROM authorities WHERE user_id = ?",
                AuthorityEntityRowMapper.instance, userId
        );
        for (AuthorityEntity authorityEntity : authorityEntityList) {
            authorityEntity.setUser(authUser);
        }
        authUser.setAuthorities(authorityEntityList);
        return Optional.of(authUser);
    }

    @Override
    public Optional<AuthUserEntity> findUserByUsername(String username) {
        AuthUserEntity authUser = authJdbcTpl.queryForObject(
                "SELECT * FROM users WHERE username = ?",
                AuthUserEntityRowMapper.instance, username
        );
        if (authUser == null) {
            return Optional.empty();
        }

        List<AuthorityEntity> authorityEntityList = authJdbcTpl.query(
                "SELECT * FROM authorities WHERE user_id = ?",
                AuthorityEntityRowMapper.instance, authUser.getId()
        );
        for (AuthorityEntity authorityEntity : authorityEntityList) {
            authorityEntity.setUser(authUser);
        }
        authUser.setAuthorities(authorityEntityList);
        return Optional.of(authUser);
    }
}
