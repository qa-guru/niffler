package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.dao.impl.AuthUserDAOHibernate;
import guru.qa.niffler.db.dao.impl.AuthUserDAOJdbc;
import guru.qa.niffler.db.dao.impl.AuthUserDAOSpringJdbc;
import guru.qa.niffler.db.entity.auth.UserEntity;
import java.util.UUID;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

// Интерфейс отвечает за порядок взаимодействия с БД (любые виды хранения данных, персистенс лэйер)
// развязать реализацию с БД и бизнес логику
public interface AuthUserDAO {

  static AuthUserDAO getImpl() {
    if ("hibernate".equals(System.getProperty("db.impl"))) {
      return new AuthUserDAOSpringJdbc();
    } else if ("spring".equals(System.getProperty("db.impl"))) {
      return new AuthUserDAOJdbc();
    } else {
//      return new AuthUserDAOJdbc();
//      return new AuthUserDAOSpringJdbc();
      return new AuthUserDAOHibernate();
    }
  }

  PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  UserEntity createUser(UserEntity user);

  UserEntity getUserById(UUID userId);

  UserEntity updateUser(UserEntity user);

  void deleteUser(UserEntity user);
}
