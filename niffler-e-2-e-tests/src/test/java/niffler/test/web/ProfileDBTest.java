package niffler.test.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import niffler.data.dao.UsersDAO;
import niffler.data.entity.UsersEntity;
import niffler.jupiter.annotation.ApiLogin;
import niffler.jupiter.annotation.DAO;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.User;
import niffler.jupiter.extension.DAOResolver;
import niffler.model.CurrencyValues;
import niffler.model.UserJson;
import niffler.page.ProfilePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static niffler.jupiter.extension.CreateUserExtension.Selector.NESTED;


@ExtendWith({DAOResolver.class})
public class ProfileDBTest extends BaseTest {

    @DAO
    private UsersDAO usersDAO;

    private CurrencyValues testedCurrency = CurrencyValues.KZT;

    @BeforeEach
    void changeCurrencyBeforeTest(@User(selector = NESTED) UserJson user) {
        UsersEntity usersEntity = usersDAO.getByUsername(user.getUserName());
        usersEntity.setCurrency(testedCurrency);
        usersDAO.updateUser(usersEntity);
    }

    @Test
    @AllureId("3")
    @ApiLogin(nifflerUser = @GenerateUser)
    void checkCurrencyTest() {
        Selenide.open(ProfilePage.URL, ProfilePage.class)
                .checkCurrency(testedCurrency);
    }
}
