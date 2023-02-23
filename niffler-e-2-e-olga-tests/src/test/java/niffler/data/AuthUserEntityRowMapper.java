package niffler.data;

import niffler.data.entity.UserAuthEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthUserEntityRowMapper implements RowMapper<UserAuthEntity> {

    @Override
    public UserAuthEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserAuthEntity userAuthEntity = new UserAuthEntity();
        userAuthEntity.setId(UUID.fromString(rs.getString("id")));
        userAuthEntity.setUsername(rs.getString("username"));
        userAuthEntity.setPassword(rs.getString("password"));
        userAuthEntity.setIsEnabled(rs.getBoolean("enabled"));
        userAuthEntity.setIsAccountNonExpired(rs.getBoolean("account_non_expired"));
        userAuthEntity.setIsAccountNonLocked(rs.getBoolean("account_non_locked"));
        userAuthEntity.setIsCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
        return userAuthEntity;
    }
}
