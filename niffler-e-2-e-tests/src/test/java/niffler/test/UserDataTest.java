package niffler.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import niffler.data.dao.UsersDAO;
import niffler.data.entity.UsersEntity;
import niffler.jupiter.annotation.DAO;
import niffler.jupiter.extension.DAOResolver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;


@ExtendWith({DAOResolver.class})
public class UserDataTest extends BaseTest {

    @DAO
    private UsersDAO usersDAO;

    private String originalCurrency;
    private String testedCurrency = "KZT";

    @BeforeEach
    void addUserDataBeforeTest() {
        UsersEntity dima = usersDAO.getByUsername("dima");
        originalCurrency = dima.getCurrency();
        dima.setCurrency(testedCurrency);
        usersDAO.updateUser(dima);

        Selenide.open("http://127.0.0.1:3000/");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue("dima");
        $("input[name='password']").setValue("12345");
        $("button[type='submit']").click();
        $(".header__title").shouldBe(Condition.visible)
                .shouldHave(Condition.text("Niffler. The coin keeper."));
    }

    @AfterEach
    void restoreUser() {
        UsersEntity dima = usersDAO.getByUsername("dima");
        dima.setCurrency(originalCurrency);
        usersDAO.updateUser(dima);
    }

    @Test
    void checkCurrencyTest() {
        $(".header__username").click();
        $("span[id^='react-select'] ").parent().should(text(testedCurrency));
    }
}
