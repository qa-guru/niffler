package guru.qa.niffler.db.dao.impl;

import static guru.qa.niffler.db.entity.userdata.CurrencyValues.RUB;

import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.jdbc.DataSourceProvider;
import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.mapper.UserEntityRowMapper;
import guru.qa.niffler.db.mapper.UserdataEntityRowMapper;
import guru.qa.niffler.db.helper.UserdataHelper;
import guru.qa.niffler.db.entity.auth.Authority;
import guru.qa.niffler.db.entity.userdata.UserDataEntity;
import guru.qa.niffler.db.entity.auth.UserEntity;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

public class AuthUserDAOSpringJdbc implements AuthUserDAO, UserDataUserDAO {

//  private final JdbcTemplate authJdbcTemplate = new JdbcTemplate(
//      DataSourceProvider.INSTANCE.getDataSource(ServiceDB.AUTH)
//  );
//  private final JdbcTemplate userdataJdbcTemplate = new JdbcTemplate(
//      DataSourceProvider.INSTANCE.getDataSource(ServiceDB.USERDATA)
//  );

  private final TransactionTemplate authTtmpl;
  private final TransactionTemplate userdataTtmpl;
  private final JdbcTemplate authJdbcTemplate;
  private final JdbcTemplate userdataJdbcTemplate;

  public AuthUserDAOSpringJdbc() {

    JdbcTransactionManager authTM = new JdbcTransactionManager(
        DataSourceProvider.INSTANCE.getDataSource(ServiceDB.AUTH)
    );
    JdbcTransactionManager userdataTM = new JdbcTransactionManager(
        DataSourceProvider.INSTANCE.getDataSource(ServiceDB.USERDATA)
    );

    this.authTtmpl = new TransactionTemplate(authTM);
    this.userdataTtmpl = new TransactionTemplate(userdataTM);
    this.authJdbcTemplate = new JdbcTemplate(authTM.getDataSource());
    this.userdataJdbcTemplate = new JdbcTemplate(userdataTM.getDataSource());
  }

  @Override
  @SuppressWarnings("unchecked")
  public UserEntity createUser(UserEntity user) {
    return authTtmpl.execute(status -> {
      KeyHolder kh = new GeneratedKeyHolder();
      authJdbcTemplate.update(con -> {
        PreparedStatement usersPs = con.prepareStatement("INSERT INTO users (username, password, "
            + "enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
            "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

        usersPs.setString(1, user.getUsername());
        usersPs.setString(2, pe.encode(user.getPassword()));
        usersPs.setBoolean(3, user.getEnabled());
        usersPs.setBoolean(4, user.getAccountNonExpired());
        usersPs.setBoolean(5, user.getAccountNonLocked());
        usersPs.setBoolean(6, user.getAccountNonExpired());

        return usersPs;
      }, kh);

      UUID userId = (UUID) kh.getKeyList().get(0).get("id");

      authJdbcTemplate.batchUpdate(
          "INSERT INTO authorities (user_id, authority) VALUES (?, ?)",
          new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
              ps.setObject(1, userId);
              ps.setObject(2, Authority.values()[i].name());
            }

            @Override
            public int getBatchSize() {
              return Authority.values().length;
            }
          }
      );

//      authJdbcTemplate.batchUpdate(
//          "INSERT INTO authorities (user_id, authority) VALUES (?, ?)",
//          List.of(
//              new Object[] {userId, Authority.read.name()},
//              new Object[] {userId, Authority.write.name()}
//          )
//      );
      user.setId(userId);
      return getUserById(user.getId());
    });
  }

  @Override
  public UserEntity getUserById(UUID userId) {
    return authJdbcTemplate.queryForObject(
        "SELECT * FROM users WHERE id = ?",
        UserEntityRowMapper.userInstance,
        userId
    );
  }

  @Override
  public UserEntity updateUser(UserEntity user) {

    authJdbcTemplate.update(
        "UPDATE users SET(password, enabled, account_non_expired, "
            + "account_non_locked, credentials_non_expired) = (?, ?, ?, ?, ?) WHERE id = ?",

        pe.encode(user.getPassword()),
        user.getEnabled(),
        user.getAccountNonExpired(),
        user.getAccountNonLocked(),
        user.getAccountNonExpired(),
        user.getId()
    );

    return getUserById(user.getId());
  }

  @Override
  public void deleteUser(UserEntity user) {
    authTtmpl.execute(status -> {
      authJdbcTemplate.update("DELETE FROM authorities WHERE user_id = ?", user.getId());
      authJdbcTemplate.update("DELETE FROM users WHERE id = ?", user.getId());
      return null;
    });
  }

  @Override
  public UserDataEntity createUserInUserData(UserDataEntity userData) {
     userdataJdbcTemplate.update(
        "INSERT INTO users (username, currency) VALUES (?, ?)",
        userData.getUsername(),
        RUB.name()
    );

    return getUserdataInUserData(userData);
  }

  @Override
  public UserDataEntity getUserdataInUserData(UserDataEntity userData) {
    return userdataJdbcTemplate.queryForObject(
        "SELECT * FROM users WHERE username = ?",
        UserdataEntityRowMapper.instance,
        userData.getUsername()
    );
  }

  @Override
  public UserDataEntity getUserdataInUserDataByUserName(String username) {
    return userdataJdbcTemplate.queryForObject(
        "SELECT * FROM users WHERE username = ?",
        UserdataEntityRowMapper.instance,
        username
    );
  }

  @Override
  public UserDataEntity updateUserInUserData(UserDataEntity userData) {

    userdataJdbcTemplate.update(
        "UPDATE users SET (currency, firstname, surname, photo) = (?, ?, ?, ?) WHERE username = ?",
        userData.getCurrency().name(),
        userData.getFirstname(),
        userData.getSurname(),
        new UserdataHelper().getEncodingPhoto(userData).getBytes(),
        userData.getUsername()
    );

    return getUserdataInUserData(userData);
  }

  @Override
  public void deleteUserByIdInUserData(UUID userId) {
    userdataJdbcTemplate.update("DELETE FROM users WHERE id = ?", userId);
  }

  @Override
  public void deleteUserByUsernameInUserData(UserDataEntity userData) {
    userdataJdbcTemplate.update("DELETE FROM users WHERE username = ?", userData.getUsername());
  }
}
