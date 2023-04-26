package niffler.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import javax.sql.DataSource;
import niffler.db.DataSourceProvider;
import niffler.db.ServiceDB;
import niffler.db.entity.AuthorityEntity;
import niffler.db.entity.UserEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

public class NifflerUsersDAOSpringJdbc implements NifflerUsersDAO {

  private final TransactionTemplate transactionTemplate;
  private final JdbcTemplate jdbcTemplate;

  public NifflerUsersDAOSpringJdbc() {
    DataSourceTransactionManager transactionManager = new JdbcTransactionManager(
        DataSourceProvider.INSTANCE.getDataSource(ServiceDB.NIFFLER_AUTH));
    this.transactionTemplate = new TransactionTemplate(transactionManager);
    this.jdbcTemplate = new JdbcTemplate(transactionManager.getDataSource());
  }

  @Override
  public int createUser(UserEntity user) {
    return 0;
  }

  @Override
  public String getUserId(String userName) {
    return jdbcTemplate.query("SELECT * FROM users WHERE username = ?",
        rs -> {return rs.getString(1);},
        userName
    );
  }

  @Override
  public int removeUser(UserEntity user) {
    return transactionTemplate.execute(st -> {
      jdbcTemplate.update("DELETE FROM authorities WHERE user_id = ?", user.getId());
      return jdbcTemplate.update("DELETE FROM users WHERE id = ?", user.getId());
    });
  }
}
