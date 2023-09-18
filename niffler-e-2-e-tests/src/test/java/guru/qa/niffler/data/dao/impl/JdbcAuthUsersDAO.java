package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthUsersDAO;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.jdbc.DataSourceContext;
import io.qameta.allure.Step;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.DataBase.AUTH;

public class JdbcAuthUsersDAO implements AuthUsersDAO {

    private static DataSource authDs = DataSourceContext.INSTANCE.getDatatSource(AUTH);

    @Step("Add user to auth database using jdbc")
    @Override
    public void createUser(AuthUserEntity user) {
        try (Connection conn = authDs.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement usersPs = conn.prepareStatement(
                    "INSERT INTO users (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                            "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);

                 PreparedStatement authorityPs = conn.prepareStatement(
                         "INSERT INTO authorities (user_id, authority) " +
                                 "VALUES (?, ?)")) {

                usersPs.setString(1, user.getUsername());
                usersPs.setString(2, pe.encode(user.getPassword()));
                usersPs.setBoolean(3, user.getEnabled());
                usersPs.setBoolean(4, user.getAccountNonExpired());
                usersPs.setBoolean(5, user.getAccountNonLocked());
                usersPs.setBoolean(6, user.getCredentialsNonExpired());

                usersPs.executeUpdate();
                UUID generatedUserId;
                try (ResultSet generatedKeys = usersPs.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedUserId = UUID.fromString(generatedKeys.getString("id"));
                    } else {
                        throw new IllegalStateException("Can`t obtain id from given ResultSet");
                    }
                }

                for (AuthorityEntity authority : user.getAuthorities()) {
                    authorityPs.setObject(1, generatedUserId);
                    authorityPs.setString(2, authority.getAuthority().name());
                    authorityPs.addBatch();
                    authorityPs.clearParameters();
                }

                authorityPs.executeBatch();
                user.setId(generatedUserId);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Update user in auth database using jdbc")
    @Override
    public AuthUserEntity updateUser(AuthUserEntity user) {
        try (Connection conn = authDs.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement usersPs = conn.prepareStatement(
                    "UPDATE users SET " +
                            "password = ?, " +
                            "enabled = ?, " +
                            "account_non_expired = ?, " +
                            "account_non_locked = ?, " +
                            "credentials_non_expired = ? " +
                            "WHERE id = ? ");

                 PreparedStatement clearAuthorityPs = conn.prepareStatement("DELETE FROM authorities WHERE user_id = ?");

                 PreparedStatement authorityPs = conn.prepareStatement(
                         "INSERT INTO authorities (user_id, authority) " +
                                 "VALUES (?, ?)")) {

                clearAuthorityPs.setObject(1, user.getId());
                clearAuthorityPs.executeUpdate();

                for (AuthorityEntity authority : user.getAuthorities()) {
                    authorityPs.setObject(1, user.getId());
                    authorityPs.setString(2, authority.getAuthority().name());
                    authorityPs.addBatch();
                    authorityPs.clearParameters();
                }
                authorityPs.executeBatch();

                usersPs.setString(1, pe.encode(user.getPassword()));
                usersPs.setBoolean(2, user.getEnabled());
                usersPs.setBoolean(3, user.getAccountNonExpired());
                usersPs.setBoolean(4, user.getAccountNonLocked());
                usersPs.setBoolean(5, user.getCredentialsNonExpired());
                usersPs.setObject(6, user.getId());
                usersPs.executeUpdate();

                conn.commit();
                return findUserById(user.getId()).get();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Remove user from auth database using jdbc")
    @Override
    public void deleteUser(AuthUserEntity user) {
        try (Connection conn = authDs.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement usersPs = conn.prepareStatement("DELETE FROM users WHERE id = ?");
                 PreparedStatement authorityPs = conn.prepareStatement("DELETE FROM authorities WHERE user_id = ?")) {

                authorityPs.setObject(1, user.getId());
                usersPs.setObject(1, user.getId());

                authorityPs.executeUpdate();
                usersPs.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Find user in auth database by user id '{userId}' using jdbc")
    @Override
    public Optional<AuthUserEntity> findUserById(UUID userId) {
        Optional<AuthUserEntity> userEntity;
        try (Connection conn = authDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("SELECT * " +
                     "FROM public.users u " +
                     "JOIN authorities a ON u.id = a.user_id " +
                     "where u.id = ?")) {
            usersPs.setObject(1, userId);

            usersPs.execute();
            userEntity = extractFromResultSet(usersPs.getResultSet());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userEntity;
    }

    @Step("Find user in auth database by username '{username}' using jdbc")
    @Override
    public Optional<AuthUserEntity> findUserByUsername(String username) {
        Optional<AuthUserEntity> userEntity;
        try (Connection conn = authDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("SELECT * " +
                     "FROM public.users u " +
                     "JOIN authorities a ON u.id = a.user_id " +
                     "where u.username = ?")) {
            usersPs.setString(1, username);
            usersPs.execute();
            userEntity = extractFromResultSet(usersPs.getResultSet());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userEntity;
    }

    private Optional<AuthUserEntity> extractFromResultSet(ResultSet resultSet) throws SQLException {
        AuthUserEntity user = new AuthUserEntity();
        boolean userNotSet = true;

        while (resultSet.next()) {
            if (userNotSet) {
                user.setId(resultSet.getObject(1, UUID.class));
                user.setUsername(resultSet.getString(2));
                user.setPassword(resultSet.getString(3));
                user.setEnabled(resultSet.getBoolean(4));
                user.setAccountNonExpired(resultSet.getBoolean(5));
                user.setAccountNonLocked(resultSet.getBoolean(6));
                user.setCredentialsNonExpired(resultSet.getBoolean(7));
                userNotSet = false;
            }

            AuthorityEntity authority = new AuthorityEntity();
            authority.setId(resultSet.getObject(8, UUID.class));
            authority.setAuthority(Authority.valueOf(resultSet.getString(10)));
            authority.setUser(user);

            user.getAuthorities().add(authority);
        }
        return userNotSet ? Optional.empty() : Optional.of(user);
    }
}
