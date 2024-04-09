package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.CurrencyValues;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import io.qameta.allure.Step;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.DataBase.AUTH;
import static guru.qa.niffler.data.DataBase.USERDATA;
import static guru.qa.niffler.data.jdbc.DataSourceContext.dataSource;

public class UserRepositoryJdbc implements UserRepository {

    private final DataSource authDs = dataSource(AUTH);
    private final DataSource userdataDs = dataSource(USERDATA);

    @Step("Create user in auth db using JDBC")
    @Override
    public AuthUserEntity createInAuth(AuthUserEntity user) {
        try (Connection conn = authDs.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement usersPs = conn.prepareStatement(
                    "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                            "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);

                 PreparedStatement authorityPs = conn.prepareStatement(
                         "INSERT INTO \"authority\" (user_id, authority) " +
                                 "VALUES (?, ?)")) {

                usersPs.setString(1, user.getUsername());
                usersPs.setString(2, user.getPassword());
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
                return user;
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

    @Step("Find user in auth db by id: '{id}' using JDBC")
    @Override
    public Optional<AuthUserEntity> findByIdInAuth(UUID id) {
        try (Connection conn = authDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("SELECT * " +
                     "FROM \"user\" u " +
                     "JOIN \"authority\" a ON u.id = a.user_id " +
                     "where u.id = ?")) {
            usersPs.setObject(1, id);

            usersPs.execute();
            return extractUserFromAuthResultSet(usersPs.getResultSet());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Create user in userdata db using JDBC")
    @Override
    public UserEntity createInUserdata(UserEntity user) {
        try (Connection conn = userdataDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement(
                     "INSERT INTO \"user\" (username, currency) VALUES (?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            usersPs.setString(1, user.getUsername());
            usersPs.setString(2, user.getCurrency().name());
            usersPs.executeUpdate();
            UUID generatedUserId;
            try (ResultSet generatedKeys = usersPs.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedUserId = UUID.fromString(generatedKeys.getString("id"));
                } else {
                    throw new IllegalStateException("Can`t obtain id from given ResultSet");
                }
            }
            user.setId(generatedUserId);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Find user in userdata db by id: '{id}' using JDBC")
    @Override
    public Optional<UserEntity> findByIdInUserdata(UUID id) {
        UserEntity user = new UserEntity();
        try (Connection conn = userdataDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("SELECT * FROM \"user\" WHERE id = ? ")) {
            usersPs.setObject(1, id);

            usersPs.execute();
            ResultSet resultSet = usersPs.getResultSet();

            if (resultSet.next()) {
                user.setId(resultSet.getObject("id", UUID.class));
                user.setUsername(resultSet.getString("username"));
                user.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                user.setFirstname(resultSet.getString("firstname"));
                user.setSurname(resultSet.getString("surname"));
                user.setPhoto(resultSet.getBytes("photo"));
                user.setPhotoSmall(resultSet.getBytes("photo_small"));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(user);
    }

    @Step("Update user in auth db using JDBC")
    @Override
    public void updateInAuth(AuthUserEntity user) {
        try (Connection conn = authDs.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement usersPs = conn.prepareStatement(
                    "UPDATE \"user\" SET " +
                            "password = ?, " +
                            "enabled = ?, " +
                            "account_non_expired = ?, " +
                            "account_non_locked = ?, " +
                            "credentials_non_expired = ? " +
                            "WHERE id = ? ");

                 PreparedStatement clearAuthorityPs = conn.prepareStatement("DELETE FROM \"authority\" WHERE user_id = ?");

                 PreparedStatement authorityPs = conn.prepareStatement(
                         "INSERT INTO \"authority\" (user_id, authority) " +
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

                usersPs.setString(1, user.getPassword());
                usersPs.setBoolean(2, user.getEnabled());
                usersPs.setBoolean(3, user.getAccountNonExpired());
                usersPs.setBoolean(4, user.getAccountNonLocked());
                usersPs.setBoolean(5, user.getCredentialsNonExpired());
                usersPs.setObject(6, user.getId());
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

    @Step("Update user in userdata db using JDBC")
    @Override
    public void updateInUserdata(UserEntity user) {
        try (Connection conn = userdataDs.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement usersPs = conn.prepareStatement(
                    "UPDATE \"user\" SET " +
                            "currency = ?, " +
                            "firstname = ?, " +
                            "surname = ?, " +
                            "photo = ?, " +
                            "photo_small = ? " +
                            "WHERE id = ? ");

                 PreparedStatement friendsPs = conn.prepareStatement(
                         "INSERT INTO friendship (requester_id, addressee_id, status) " +
                                 "VALUES (?, ?, ?) " +
                                 "ON CONFLICT (requester_id, addressee_id) " +
                                 "DO UPDATE SET status = ?")
            ) {
                usersPs.setString(1, user.getCurrency().name());
                usersPs.setString(2, user.getFirstname());
                usersPs.setString(3, user.getSurname());
                usersPs.setBytes(4, user.getPhoto());
                usersPs.setBytes(5, user.getPhotoSmall());
                usersPs.setObject(6, user.getId());
                usersPs.executeUpdate();

                for (FriendshipEntity fe : user.getFriendshipRequests()) {
                    friendsPs.setObject(1, user.getId());
                    friendsPs.setObject(2, fe.getAddressee().getId());
                    friendsPs.setString(3, fe.getStatus().name());
                    friendsPs.setString(4, fe.getStatus().name());
                    friendsPs.addBatch();
                    friendsPs.clearParameters();
                }
                friendsPs.executeBatch();

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

    @Step("Remove user from auth db using JDBC")
    @Override
    public void deleteInAuth(AuthUserEntity user) {
        try (Connection conn = authDs.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement usersPs = conn.prepareStatement("DELETE FROM \"user\" WHERE id = ?");
                 PreparedStatement authorityPs = conn.prepareStatement("DELETE FROM \"authority\" WHERE user_id = ?")) {

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

    @Step("Remove user from userdata db using JDBC")
    @Override
    public void deleteInUserdata(UserEntity user) {
        try (Connection conn = userdataDs.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement usersPs = conn.prepareStatement("DELETE FROM \"user\" WHERE id = ?");
                 PreparedStatement friendsPs = conn.prepareStatement("DELETE FROM friendship WHERE requester_id = ?");
                 PreparedStatement invitesPs = conn.prepareStatement("DELETE FROM friendship WHERE addressee_id = ?")
            ) {
                usersPs.setObject(1, user.getId());
                friendsPs.setObject(1, user.getId());
                invitesPs.setObject(1, user.getId());
                friendsPs.executeUpdate();
                invitesPs.executeUpdate();
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

    private Optional<AuthUserEntity> extractUserFromAuthResultSet(ResultSet resultSet) throws SQLException {
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
            user.addAuthorities(authority);
        }
        return userNotSet ? Optional.empty() : Optional.of(user);
    }
}
