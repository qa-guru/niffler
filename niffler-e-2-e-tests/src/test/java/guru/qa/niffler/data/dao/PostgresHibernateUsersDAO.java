package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.UsersEntity;
import guru.qa.niffler.data.jpa.EmfContext;
import guru.qa.niffler.data.jpa.JpaService;
import io.qameta.allure.Step;

public class PostgresHibernateUsersDAO extends JpaService implements UsersDAO {
    public PostgresHibernateUsersDAO() {
        super(EmfContext.INSTANCE.getEmf(DataBase.USERDATA).createEntityManager());
    }

    @Step("Add user to database using Hibernate")
    @Override
    public int addUser(UsersEntity users) {
        persist(users);
        return 0;
    }

    @Step("Update user in database using Hibernate")
    @Override
    public void updateUser(UsersEntity user) {
        merge(user);
    }

    @Step("Remove user from database using Hibernate")
    @Override
    public void remove(UsersEntity user) {
        super.remove(user);
    }

    @Step("Get user from database by username '{username}' using Hibernate")
    @Override
    public UsersEntity getByUsername(String username) {
        return em.createQuery("select u from UsersEntity u where u.username=:username", UsersEntity.class)
                .setParameter("username", username)
                .getSingleResult();
    }
}
