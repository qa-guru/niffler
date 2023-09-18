package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.dao.impl.JdbcAuthUsersDAO;
import guru.qa.niffler.data.dao.impl.JdbcUserdataUsersDAO;

public class UserRepositoryJdbc extends AbstractUserRepository {
    public UserRepositoryJdbc() {
        super(new JdbcAuthUsersDAO(), new JdbcUserdataUsersDAO());
    }
}
