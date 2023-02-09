package guru.qa.niffler;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;

public class NifflerLoginTest {

  @Test
  void mainPageShouldBeDisplayedAfterSuccessLoginHW() {

    Selenide.open("http://127.0.0.1:3000/");
    $("a[href='/redirect']").click();
    $("input[name='username']").setValue("Rustam");
    $("input[name='password']").setValue("1234");
    $("button[type='submit']").click();
    $(".header__title").shouldBe(Condition.visible)
        .shouldHave(Condition.text("Niffler. The coin keeper."));
  }

}
