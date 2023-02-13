package niffler.data.jpa;

import niffler.data.DataBase;
import niffler.data.UsersDAO;
import niffler.data.model.UsersEntity;

public class PostgresHibernateUsersDAO extends JpaService implements UsersDAO {
    public PostgresHibernateUsersDAO() {
        super(EmfContext.INSTANCE.getEmf(DataBase.USERDATA).createEntityManager());
    }

    @Override
    public int addUser(UsersEntity users) {
        persist(users);
        return 0;
    }

    @Override
    public void updateUser(UsersEntity user) {
        merge(user);
    }

    @Override
    public void remove(UsersEntity user) {
        remove(user);
    }

    @Override
    public UsersEntity getByUsername(String username) {
        return em.createQuery("select u from UsersEntity u where u.username=:username", UsersEntity.class)
                .setParameter("username", username)
                .getSingleResult();
    }
}
