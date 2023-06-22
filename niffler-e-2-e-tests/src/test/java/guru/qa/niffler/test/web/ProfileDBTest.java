package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.data.dao.UsersDAO;
import guru.qa.niffler.data.entity.UsersEntity;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.DAO;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.ProfilePage;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.extension.CreateUserExtension.Selector.NESTED;

@Epic("[WEB][niffler-frontend]: Профиль, DB preconditions")
@DisplayName("[WEB][niffler-frontend]: Профиль, DB preconditions")
public class ProfileDBTest extends BaseWebTest {

    @DAO
    private UsersDAO usersDAO;

    private final CurrencyValues testedCurrency = CurrencyValues.KZT;

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
