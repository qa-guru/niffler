package niffler.db.dao;

import niffler.db.ServiceDB;
import niffler.db.entity.UserEntity;
import niffler.db.jpa.EmfProvider;
import niffler.db.jpa.JpaTransactionManager;

public class NifflerUsersDAOHibernate extends JpaTransactionManager implements NifflerUsersDAO {

  public NifflerUsersDAOHibernate() {
    super(EmfProvider.INSTANCE.getEmf(ServiceDB.NIFFLER_AUTH).createEntityManager());
  }

  @Override
  public int createUser(UserEntity user) {
    user.setPassword(pe.encode(user.getPassword()));
    persist(user);
    return 0;
  }

  @Override
  public String getUserId(String userName) {
    return em.createQuery("select u from UserEntity u where username=:username", UserEntity.class)
        .setParameter("username", userName)
        .getSingleResult()
        .getId()
        .toString();
  }

  @Override
  public int removeUser(UserEntity user) {
    remove(user);
    return 0;
  }
}
