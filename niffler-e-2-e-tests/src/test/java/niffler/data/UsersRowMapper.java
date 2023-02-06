package niffler.data;

import niffler.data.model.Users;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UsersRowMapper implements RowMapper<Users> {
    @Override
    public Users mapRow(ResultSet rs, int rowNum) throws SQLException {
        Users result = new Users();
        result.setId(UUID.fromString(rs.getString(1)));
        result.setUsername(rs.getString(2));
        result.setCurrency(rs.getString(3));
        result.setFirstname(rs.getString("firstname"));
        result.setSurname(rs.getString("surname"));
        result.setPhoto(rs.getBytes("photo"));
        return result;
    }
}
