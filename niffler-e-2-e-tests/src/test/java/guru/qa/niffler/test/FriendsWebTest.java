package guru.qa.niffler.test;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SENT;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Disabled
@ExtendWith(UsersQueueExtension.class)
public class FriendsWebTest extends BaseWebTest {

  @AllureId("102")
  @Test
  void friendsShouldBeVisible0(@User(userType = WITH_FRIENDS) UserJson user) {
    Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
    $("a[href*='redirect']").click();
    $("input[name='username']").setValue(user.getUsername());
    $("input[name='password']").setValue(user.getPassword());
    $("button[type='submit']").click();

    $("a[href*='friends']").click();
    $$(".table tbody tr").shouldHave(sizeGreaterThan(0));
  }

  @AllureId("103")
  @Test
  void friendsShouldBeVisible1(@User(userType = INVITATION_SENT) UserJson user) {
    Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
    $("a[href*='redirect']").click();
    $("input[name='username']").setValue(user.getUsername());
    $("input[name='password']").setValue(user.getPassword());
    $("button[type='submit']").click();

    $("a[href*='people']").click();
    $$(".table tbody tr").find(Condition.text("Pending invitation"))
        .should(Condition.visible);
  }

}
