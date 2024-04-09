package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import io.qameta.allure.Step;
import jakarta.persistence.EntityManager;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.DataBase.AUTH;
import static guru.qa.niffler.data.DataBase.USERDATA;
import static guru.qa.niffler.data.jpa.EmfContext.entityManager;

public class UserRepositoryHibernate implements UserRepository {

    private final EntityManager authEm = entityManager(AUTH);
    private final EntityManager userdataEm = entityManager(USERDATA);

    @Step("Create user in auth db using Hibernate")
    @Override
    public AuthUserEntity createInAuth(AuthUserEntity user) {
        authEm.persist(user);
        return user;
    }

    @Step("Find user in auth db by id: '{id}' using Hibernate")
    @Override
    public Optional<AuthUserEntity> findByIdInAuth(UUID id) {
        return Optional.ofNullable(
                authEm.find(
                        AuthUserEntity.class, id
                )
        );
    }

    @Step("Create user in userdata db using Hibernate")
    @Override
    public UserEntity createInUserdata(UserEntity user) {
        userdataEm.persist(user);
        return user;
    }

    @Step("Find user in userdata db by id: '{id}' using Hibernate")
    @Override
    public Optional<UserEntity> findByIdInUserdata(UUID id) {
        return Optional.ofNullable(
                userdataEm.find(
                        UserEntity.class, id
                )
        );
    }

    @Step("Update user in auth db using Hibernate")
    @Override
    public void updateInAuth(AuthUserEntity user) {
        authEm.merge(user);
    }

    @Step("Update user in userdata db using Hibernate")
    @Override
    public void updateInUserdata(UserEntity user) {
        userdataEm.merge(user);
    }

    @Step("Remove user from auth db using Hibernate")
    @Override
    public void deleteInAuth(AuthUserEntity user) {
        authEm.remove(user);
    }

    @Step("Remove user from userdata db using Hibernate")
    @Override
    public void deleteInUserdata(UserEntity user) {
        userdataEm.remove(user);
    }
}
