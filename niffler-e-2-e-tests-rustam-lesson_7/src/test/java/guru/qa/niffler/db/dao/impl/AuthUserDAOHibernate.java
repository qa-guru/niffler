package guru.qa.niffler.db.dao.impl;

import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.jpa.EntityManagerFactoryProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.entity.auth.UserEntity;
import java.util.UUID;

public class AuthUserDAOHibernate extends JpaService implements AuthUserDAO {

  public AuthUserDAOHibernate() {
    super(EntityManagerFactoryProvider.INSTANCE.getEntityManagerFactory(ServiceDB.AUTH).createEntityManager());
  }

  @Override
  public UserEntity createUser(UserEntity user) {
    user.setPassword(pe.encode(user.getPassword()));
    persist(user);
    return user;
  }

//  @Override тоже работает
//  public UserEntity getUserById(UUID userId) {
//    return em.createQuery("select u from UserEntity u where u.id=:userId", UserEntity.class)
//        .setParameter("userId", userId)
//        .getSingleResult();
//  }

  @Override
  public UserEntity getUserById(UUID userId) {
    return find(UserEntity.class, userId);
  }

  @Override
  public UserEntity updateUser(UserEntity user) {
    return merge(user);
  }

  @Override
  public void deleteUser(UserEntity user) {
    remove(user);
  }
}
