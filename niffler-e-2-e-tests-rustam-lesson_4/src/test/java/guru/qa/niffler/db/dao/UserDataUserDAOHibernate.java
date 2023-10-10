package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.jpa.EntityManagerFactoryProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.mapper.UserdataHelper;
import guru.qa.niffler.db.model.auth.UserEntity;
import guru.qa.niffler.db.model.userdata.UserDataEntity;
import java.io.File;
import java.util.Base64;
import java.util.UUID;
import org.apache.commons.io.FileUtils;

public class UserDataUserDAOHibernate extends JpaService implements UserDataUserDAO {

  public UserDataUserDAOHibernate() {
    super(EntityManagerFactoryProvider.INSTANCE.getEntityManagerFactory(ServiceDB.USERDATA).createEntityManager());
  }

  @Override
  public UserDataEntity createUserInUserData(UserDataEntity userData) {
    userData.setPhoto(new UserdataHelper().getEncodingPhoto(userData).getBytes());
    persist(userData);
    return userData;
  }

//  @Override // тоже работает
//  public UserDataEntity getUserdataInUserData(UserDataEntity userData) {
//    return em.createQuery("select u from UserDataEntity u where u.username=:username", UserDataEntity.class)
//        .setParameter("username", userData.getUsername())
//        .getSingleResult();
//  }

  @Override
  public UserDataEntity getUserdataInUserData(UserDataEntity userData) {
    return find(UserDataEntity.class, userData.getId());
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
