package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.dao.impl.HibernateAuthUsersDAO;
import guru.qa.niffler.data.dao.impl.HibernateUserdataUsersDAO;

public class UserRepositoryHibernate extends AbstractUserRepository {
    public UserRepositoryHibernate() {
        super(new HibernateAuthUsersDAO(), new HibernateUserdataUsersDAO());
    }
}
