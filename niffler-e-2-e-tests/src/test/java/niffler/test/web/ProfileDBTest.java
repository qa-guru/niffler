package niffler.test.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import niffler.data.dao.UsersDAO;
import niffler.data.entity.UsersEntity;
import niffler.jupiter.annotation.ApiLogin;
import niffler.jupiter.annotation.DAO;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.User;
import niffler.model.rest.CurrencyValues;
import niffler.model.rest.UserJson;
import niffler.page.ProfilePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static niffler.jupiter.extension.CreateUserExtension.Selector.NESTED;

@Epic("[WEB][niffler-frontend]: Профиль, DB preconditions")
@DisplayName("[WEB][niffler-frontend]: Профиль, DB preconditions")
public class ProfileDBTest extends BaseWebTest {

    @DAO
    private UsersDAO usersDAO;

    private CurrencyValues testedCurrency = CurrencyValues.KZT;

    @BeforeEach
    void changeCurrencyBeforeTest(@User(selector = NESTED) UserJson user) {
        UsersEntity usersEntity = usersDAO.getByUsername(user.getUsername());
        usersEntity.setCurrency(testedCurrency);
        usersDAO.updateUser(usersEntity);
    }

    @Test
    @AllureId("500003")
    @DisplayName("WEB: В профиле должна отображаться валюта, сохраненная в niffler-userdata")
    @Tag("WEB")
    @ApiLogin(nifflerUser = @GenerateUser)
    void checkCurrencyTest() {
        Selenide.open(ProfilePage.URL, ProfilePage.class)
                .checkCurrency(testedCurrency);
    }
}
