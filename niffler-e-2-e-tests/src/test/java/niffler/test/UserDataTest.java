package niffler.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import niffler.data.jpa.query.PostgresHibernateUsers;
import niffler.data.model.FriendsEntity;
import niffler.data.model.dao.FriendsDAO;
import niffler.data.model.dao.UsersDAO;
import niffler.data.model.UsersEntity;
import niffler.jupiter.DAO;
import niffler.jupiter.DAOResolver;
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

    @DAO
    private FriendsDAO friendsDAO;

    private String originalCurrency;
    private String testedCurrency = "KZT";

    @BeforeEach
    void addUserDataBeforeTest() {
        UsersEntity dima = usersDAO.getByUsername("artem");
        originalCurrency = dima.getCurrency();
        dima.setCurrency(testedCurrency);


        FriendsEntity friends = friendsDAO.getByFriendsName("Petr");
        friends.setName("Vova");
        friendsDAO.updateFriends(friends);


        Selenide.open("http://127.0.0.1:3000/");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue("artem");
        $("input[name='password']").setValue("123");
        $("button[type='submit']").click();
        $(".header__title").shouldBe(Condition.visible)
                .shouldHave(Condition.text("Niffler. The coin keeper."));
    }

    @AfterEach
    void restoreUser() {

        UsersEntity dima = usersDAO.getByUsername("artem");
        dima.setCurrency(originalCurrency);
        usersDAO.updateUser(dima);


        FriendsEntity friends = friendsDAO.getByFriendsName("Vova");
        friends.setName("Petr");
        friendsDAO.updateFriends(friends);

        System.out.println(dima.getFriendsEntity().getName());



    }

    @Test
    void checkCurrencyTest() {
        $(".header__username").click();
        $("span[id^='react-select'] ").parent().should(text(testedCurrency));
    }
}
