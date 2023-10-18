package guru.qa.niffler.db.mapper;

import guru.qa.niffler.db.entity.userdata.CurrencyValues;
import guru.qa.niffler.db.entity.userdata.UserDataEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;

public class UserdataEntityRowMapper implements RowMapper<UserDataEntity> {

  public static final UserdataEntityRowMapper instance = new UserdataEntityRowMapper();

  @Override
  public UserDataEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

    UserDataEntity userData = new UserDataEntity();

    userData.setId(rs.getObject("id", UUID.class));
    userData.setUsername(rs.getString("username"));
    userData.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
    userData.setFirstname(rs.getString("firstname"));
    userData.setSurname(rs.getString("surname"));
//    userData.setPhoto(rs.getString("photo"));
    userData.setPhoto(rs.getBytes("photo"));
    return userData;
  }
}
