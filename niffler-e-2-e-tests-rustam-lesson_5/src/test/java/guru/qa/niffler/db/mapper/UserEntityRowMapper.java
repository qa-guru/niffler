package guru.qa.niffler.db.mapper;

import guru.qa.niffler.db.entity.auth.UserEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;

public class UserEntityRowMapper implements RowMapper<UserEntity> {

  public static final UserEntityRowMapper userInstance = new UserEntityRowMapper();

  @Override
  public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

    UserEntity user = new UserEntity();
    user.setId(rs.getObject("id", UUID.class));
    user.setUsername(rs.getString("username"));
    user.setPassword(rs.getString("password"));
    user.setEnabled(rs.getBoolean("enabled"));
    user.setAccountNonExpired(rs.getBoolean("account_non_expired"));
    user.setAccountNonLocked(rs.getBoolean("account_non_locked"));
    user.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
    return user;
  }
}
