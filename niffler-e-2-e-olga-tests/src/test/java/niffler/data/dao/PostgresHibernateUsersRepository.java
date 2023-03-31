package niffler.data.dao;

import niffler.data.DataBase;
import niffler.data.entity.UsersEntity;
import niffler.data.jpa.EmfContext;
import niffler.data.jpa.JpaService;

public class PostgresHibernateUsersRepository extends JpaService implements UsersRepository {
    public PostgresHibernateUsersRepository() {
        super(EmfContext.INSTANCE.getEmf(DataBase.USERDATA).createEntityManager());
    }

    @Override
    public void addUser(UsersEntity users) {
        persist(users);
    }

    @Override
    public void updateUser(UsersEntity user) {
        merge(user);
    }

    @Override
    public void removeUser(UsersEntity user) {
        remove(user);
    }

    @Override
    public UsersEntity getByUsername(String username) {
        return em.createQuery("select u from UsersEntity u where u.username=:username", UsersEntity.class)
                .setParameter("username", username)
                .getSingleResult();
    }
}