package guru.qa.niffler.test.hw_part_3;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_RECEIVED;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SENT;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.UserQueueExtensionHW;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.test.BaseWebTest;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UserQueueExtensionHW.class)
public class FriendsWebTestHW extends BaseWebTest {

    @AllureId("101")
    @Test
    void friendsShouldBeVisible1(
        @User(userType = WITH_FRIENDS) UserJson user1,
        @User(userType = WITH_FRIENDS) UserJson user2
    ) {
        Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user1.getUsername());
        $("input[name='password']").setValue(user2.getPassword());
        $("button[type='submit']").click();

    }

    @AllureId("102")
    @Test
    void friendsShouldBeVisible2(
        @User(userType = WITH_FRIENDS) UserJson user1,
        @User(userType = INVITATION_SENT) UserJson user2
    ) {
        Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user1.getUsername());
        $("input[name='password']").setValue(user2.getPassword());
        $("button[type='submit']").click();

    }

    @AllureId("103")
    @Test
    void friendsShouldBeVisible3(
        @User(userType = INVITATION_RECEIVED) UserJson user1,
        @User(userType = INVITATION_SENT) UserJson user2
    ) {
        Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user1.getUsername());
        $("input[name='password']").setValue(user2.getPassword());
        $("button[type='submit']").click();

    }

    @AllureId("104")
    @Test
    void friendsShouldBeVisible4(
        @User(userType = INVITATION_RECEIVED) UserJson user1,
        @User(userType = INVITATION_RECEIVED) UserJson user2
    ) {
        Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user1.getUsername());
        $("input[name='password']").setValue(user2.getPassword());
        $("button[type='submit']").click();

    }
}
