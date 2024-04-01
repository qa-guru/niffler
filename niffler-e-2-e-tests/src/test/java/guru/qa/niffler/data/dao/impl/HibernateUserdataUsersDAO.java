package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.UserdataUsersDAO;
import guru.qa.niffler.data.entity.ud.UserEntity;
import guru.qa.niffler.data.jpa.EmfContext;
import guru.qa.niffler.data.jpa.JpaService;
import io.qameta.allure.Step;
import jakarta.persistence.NoResultException;

import java.util.Optional;

import static guru.qa.niffler.data.DataBase.USERDATA;

public class HibernateUserdataUsersDAO extends JpaService implements UserdataUsersDAO {

    public HibernateUserdataUsersDAO() {
        super(EmfContext.INSTANCE.getEmf(USERDATA).createEntityManager());
    }

    @Step("Add user to userdata database using Hibernate")
    @Override
    public void createUser(UserEntity user) {
        persist(user);
    }

    @Step("Update user in userdata database using Hibernate")
    @Override
    public UserEntity updateUser(UserEntity user) {
        return merge(user);
    }

    @Step("Remove user from userdata database using Hibernate")
    @Override
    public void deleteUser(UserEntity user) {
        remove(user);
    }

    @Step("Find user in userdata database by username '{username}' using Hibernate")
    @Override
    public Optional<UserEntity> findUserByUsername(String username) {
        try {
            return Optional.of(
                    em.createQuery("select u from UserEntity u where u.username=:username", UserEntity.class)
                            .setParameter("username", username)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
