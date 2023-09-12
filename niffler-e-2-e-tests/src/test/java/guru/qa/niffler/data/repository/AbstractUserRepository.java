package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.dao.AuthUsersDAO;
import guru.qa.niffler.data.dao.UserdataUsersDAO;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.ud.CurrencyValues;
import guru.qa.niffler.data.entity.ud.UserEntity;
import io.qameta.allure.Step;

import java.util.Optional;

public abstract class AbstractUserRepository implements UserRepository {

    private final AuthUsersDAO authUserDAO;
    private final UserdataUsersDAO udUserDAO;

    protected AbstractUserRepository(AuthUsersDAO authUserDAO, UserdataUsersDAO udUserDAO) {
        this.authUserDAO = authUserDAO;
        this.udUserDAO = udUserDAO;
    }

    @Step("Create user in auth & userdata")
    @Override
    public void createUserForTest(AuthUserEntity user) {
        authUserDAO.createUser(user);
        udUserDAO.createUser(fromAuthUser(user));
    }

    @Override
    public AuthUserEntity getTestUserFromAuth(String username) {
        return authUserDAO.findUserByUsername(username).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public UserEntity getTestUserFromUserdata(String username) {
        return udUserDAO.findUserByUsername(username).orElseThrow(IllegalArgumentException::new);
    }

    @Step("Update user in auth db")
    @Override
    public void updateUserForTest(AuthUserEntity user) {
        authUserDAO.updateUser(user);
    }

    @Step("Update user in userdata db")
    @Override
    public void updateUserForTest(UserEntity user) {
        udUserDAO.updateUser(user);
    }

    @Step("Remove user from auth & userdata")
    @Override
    public void removeAfterTest(AuthUserEntity user) {
        Optional<UserEntity> userInUd = udUserDAO.findUserByUsername(user.getUsername());
        if (userInUd.isPresent()) {
            udUserDAO.deleteUser(userInUd.get());
            authUserDAO.deleteUser(user);
        } else {
            throw new RuntimeException("Can`t find user in userdata by username: " + user.getUsername());
        }
    }

    private UserEntity fromAuthUser(AuthUserEntity user) {
        UserEntity userdataUser = new UserEntity();
        userdataUser.setUsername(user.getUsername());
        userdataUser.setCurrency(CurrencyValues.RUB);
        return userdataUser;
    }
}
