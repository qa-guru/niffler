package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.dao.impl.SpringJdbcAuthUsersDAO;
import guru.qa.niffler.data.dao.impl.SpringJdbcUserdataUsersDAO;

public class UserRepositorySpringJdbc extends AbstractUserRepository {
    public UserRepositorySpringJdbc() {
        super(new SpringJdbcAuthUsersDAO(), new SpringJdbcUserdataUsersDAO());
    }
}
