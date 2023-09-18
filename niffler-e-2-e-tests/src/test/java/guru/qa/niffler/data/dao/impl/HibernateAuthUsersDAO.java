package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthUsersDAO;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.jpa.EmfContext;
import guru.qa.niffler.data.jpa.JpaService;
import io.qameta.allure.Step;
import jakarta.persistence.NoResultException;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.DataBase.AUTH;

public class HibernateAuthUsersDAO extends JpaService implements AuthUsersDAO {

    public HibernateAuthUsersDAO() {
        super(EmfContext.INSTANCE.getEmf(AUTH).createEntityManager());
    }

    @Step("Add user to auth database using Hibernate")
    @Override
    public void createUser(AuthUserEntity user) {
        user.setPassword(pe.encode(user.getPassword()));
        persist(user);
    }

    @Step("Update user in auth database using Hibernate")
    @Override
    public AuthUserEntity updateUser(AuthUserEntity user) {
        return merge(user);
    }

    @Step("Remove user from auth database using Hibernate")
    @Override
    public void deleteUser(AuthUserEntity user) {
        remove(user);
    }

    @Step("Find user in auth database by user id '{userId}' using Hibernate")
    @Override
    public Optional<AuthUserEntity> findUserById(UUID userId) {
        try {
            return Optional.of(
                    em.createQuery("select u from AuthUserEntity u where u.id=:userId", AuthUserEntity.class)
                            .setParameter("id", userId)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Step("Find user in auth database by username '{username}' using Hibernate")
    @Override
    public Optional<AuthUserEntity> findUserByUsername(String username) {
        try {
            return Optional.of(
                    em.createQuery("select u from AuthUserEntity u where u.username=:username", AuthUserEntity.class)
                            .setParameter("username", username)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
