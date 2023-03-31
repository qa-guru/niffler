package niffler.data.spring_jdbc;

import niffler.data.entity.UsersEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UsersRowMapper implements RowMapper<UsersEntity> {
    @Override
    public UsersEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UsersEntity result = new UsersEntity();
        result.setId(UUID.fromString(rs.getString(1)));
        result.setUsername(rs.getString(2));
        result.setCurrency(rs.getString(3));
        result.setFirstname(rs.getString("firstname"));
        result.setSurname(rs.getString("surname"));
        result.setPhoto(rs.getBytes("photo"));
        return result;
    }
}
