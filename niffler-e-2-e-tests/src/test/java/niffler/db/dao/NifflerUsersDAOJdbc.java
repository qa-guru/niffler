package niffler.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.sql.DataSource;
import niffler.db.DataSourceProvider;
import niffler.db.ServiceDB;
import niffler.db.entity.UserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class NifflerUsersDAOJdbc implements NifflerUsersDAO {

  private static final DataSource ds = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.NIFFLER_AUTH);
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  @Override
  public int createUser(UserEntity user) {
    int executeUpdate;

    try (Connection conn = ds.getConnection();
        PreparedStatement st = conn.prepareStatement("INSERT INTO users "
            + "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) "
            + " VALUES (?, ?, ?, ?, ?, ?)")) {
      st.setString(1, user.getUsername());
      st.setString(2, pe.encode(user.getPassword()));
      st.setBoolean(3, user.getEnabled());
      st.setBoolean(4, user.getAccountNonExpired());
      st.setBoolean(5, user.getAccountNonLocked());
      st.setBoolean(6, user.getCredentialsNonExpired());

      executeUpdate = st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    String insertAuthoritiesSql = "INSERT INTO authorities (user_id, authority) VALUES ('%s', '%s')";

    final String finalUserId = getUserId(user.getUsername());
    List<String> sqls = user.getAuthorities()
        .stream()
        .map(ae -> ae.getAuthority().name())
        .map(a -> String.format(insertAuthoritiesSql, finalUserId, a))
        .toList();

    for (String sql : sqls) {
      try (Connection conn = ds.getConnection();
          Statement st = conn.createStatement()) {
        st.executeUpdate(sql);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
    return executeUpdate;
  }

  @Override
  public String getUserId(String userName) {
    try (Connection conn = ds.getConnection();
        PreparedStatement st = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
      st.setString(1, userName);
      ResultSet resultSet = st.executeQuery();
      if (resultSet.next()) {
        return resultSet.getString(1);
      } else {
        throw new IllegalArgumentException("Can`t find user by given username: " + userName);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
