package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.UserdataUsersDAO;
import guru.qa.niffler.data.entity.ud.CurrencyValues;
import guru.qa.niffler.data.entity.ud.FriendsEntity;
import guru.qa.niffler.data.entity.ud.UserEntity;
import guru.qa.niffler.data.jdbc.DataSourceContext;
import io.qameta.allure.Step;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.DataBase.USERDATA;

public class JdbcUserdataUsersDAO implements UserdataUsersDAO {

    private static DataSource userdataDs = DataSourceContext.INSTANCE.getDatatSource(USERDATA);

    @Step("Add user to userdata database using jdbc")
    @Override
    public void createUser(UserEntity user) {
        try (Connection conn = userdataDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement(
                     "INSERT INTO users (username, currency) VALUES (?, ?)")
        ) {
            usersPs.setString(1, user.getUsername());
            usersPs.setString(2, CurrencyValues.RUB.name());
            usersPs.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Update user in userdata database using jdbc")
    @Override
    public UserEntity updateUser(UserEntity user) {
        try (Connection conn = userdataDs.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement usersPs = conn.prepareStatement(
                    "UPDATE users SET " +
                            "currency = ?, " +
                            "firstname = ?, " +
                            "surname = ?, " +
                            "photo = ? " +
                            "WHERE id = ? ");

                 PreparedStatement friendsPs = conn.prepareStatement(
                         "INSERT INTO friends (user_id, friend_id, pending) " +
                                 "VALUES (?, ?, ?) " +
                                 "ON CONFLICT (user_id, friend_id) " +
                                 "DO UPDATE SET pending = ?")
            ) {
                usersPs.setString(1, user.getCurrency().name());
                usersPs.setString(2, user.getFirstname());
                usersPs.setString(3, user.getSurname());
                usersPs.setBytes(4, user.getPhoto());
                usersPs.setObject(5, user.getId());
                usersPs.executeUpdate();

                for (FriendsEntity fe : user.getFriends()) {
                    friendsPs.setObject(1, user.getId());
                    friendsPs.setObject(2, fe.getFriend().getId());
                    friendsPs.setBoolean(3, fe.isPending());
                    friendsPs.setBoolean(4, fe.isPending());
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
            return findUserByUsername(user.getUsername()).get();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Remove user from userdata database using jdbc")
    @Override
    public void deleteUser(UserEntity user) {
        try (Connection conn = userdataDs.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement usersPs = conn.prepareStatement("DELETE FROM users WHERE id = ?");
                 PreparedStatement friendsPs = conn.prepareStatement("DELETE FROM friends WHERE user_id = ?");
                 PreparedStatement invitesPs = conn.prepareStatement("DELETE FROM friends WHERE friend_id = ?")
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

    @Step("Find user in userdata database by username '{username}' using jdbc")
    @Override
    public Optional<UserEntity> findUserByUsername(String username) {
        UserEntity user = new UserEntity();
        try (Connection conn = userdataDs.getConnection();
             PreparedStatement usersPs = conn.prepareStatement("SELECT * FROM users WHERE username = ? ")) {
            usersPs.setObject(1, username);

            usersPs.execute();
            ResultSet resultSet = usersPs.getResultSet();

            if (resultSet.next()) {
                user.setId(resultSet.getObject("id", UUID.class));
                user.setUsername(resultSet.getString("username"));
                user.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                user.setFirstname(resultSet.getString("firstname"));
                user.setSurname(resultSet.getString("surname"));
                user.setPhoto(resultSet.getBytes("photo"));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(user);
    }
}
