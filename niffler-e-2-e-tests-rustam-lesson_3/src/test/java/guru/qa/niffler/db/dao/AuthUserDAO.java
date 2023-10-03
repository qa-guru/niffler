package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.UserEntity;
import java.util.UUID;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

// Интерфейс отвечает за порядок взаимодействия с БД (любые виды хранения данных, персистенс лэйер)
// развязать реализацию с БД и бизнес логику
public interface AuthUserDAO {

//  static AuthUserDAO getImpl() {
//    if ("hibernate".equals(System.getProperty("db.impl"))) {
//      return new AuthUserDAOHibernate();
//    } else if ("spring".equals(System.getProperty("db.impl"))) {
//      return new AuthUserDAOSpringJdbc();
//    } else {
//      return new AuthUserDAOJdbc();
//    }
//  }

  PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  int createUser(UserEntity user);

  UserEntity getUserById(UUID userId);

  void updateUser(UserEntity user);

  void deleteUserById(UUID userId);
}
