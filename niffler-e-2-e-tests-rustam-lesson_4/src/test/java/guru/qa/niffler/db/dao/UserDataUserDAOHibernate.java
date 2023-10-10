package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.jpa.EntityManagerFactoryProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.userdata.UserDataEntity;
import java.util.UUID;

public class UserDataUserDAOHibernate extends JpaService implements UserDataUserDAO {

  public UserDataUserDAOHibernate() {
    super(EntityManagerFactoryProvider.INSTANCE.getEntityManagerFactory(ServiceDB.USERDATA).createEntityManager());
  }

  @Override
  public UserDataEntity createUserInUserData(UserDataEntity userData) {
    persist(userData);
    return userData;
  }

  @Override
  public UserDataEntity getUserdataInUserData(UserDataEntity userData) {
    return em.createQuery("select u from UserDataEntity u where u.username=:username", UserDataEntity.class)
        .setParameter("username", userData.getUsername())
        .getSingleResult();
  }

  @Override
  public UserDataEntity updateUserInUserData(UserDataEntity userData) {
    return merge(userData);
  }

  @Override
  public void deleteUserByIdInUserData(UUID userId) {

  }

  @Override
  public void deleteUserByUsernameInUserData(UserDataEntity userData) {
    remove(userData);
  }
}
