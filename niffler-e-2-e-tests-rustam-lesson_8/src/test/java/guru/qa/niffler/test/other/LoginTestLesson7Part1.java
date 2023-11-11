package guru.qa.niffler.test.other;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.entity.auth.UserEntity;
import guru.qa.niffler.jupiter.annotation.DAO;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.extension.DaoExtension;
import guru.qa.niffler.test.BaseWebTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DaoExtension.class)
public class LoginTestLesson7Part1 extends BaseWebTest {

    @DAO
    private AuthUserDAO authUserDAO;
    @DAO
    private UserDataUserDAO userDataUserDAO;

    @GenerateUser()
    @Test
    void mainPageShouldBeVisibleAfterLogin(UserEntity user) {
        open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue("12345");
        $("button[type='submit']").click();
        $(".main-content__section-stats").should(visible);
    }
}
