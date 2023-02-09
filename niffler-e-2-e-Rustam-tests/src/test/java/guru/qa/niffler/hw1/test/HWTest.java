package guru.qa.niffler.hw1.test;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.jupiter.MyFullExtension.body;

import com.codeborne.selenide.Condition;
import guru.qa.niffler.BaseTest;
import guru.qa.niffler.jupiter.MyFullExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MyFullExtension.class)
@Tag("spendBlock")
public class HWTest extends BaseTest {

  @BeforeAll
  static void setUp() {
    baseUrl = "http://127.0.0.1:3000/";
  }

  @Test
  @DisplayName("Checking the spend table")
  void spendsShouldBeDisplayedAfterSuccessAdding() {

    open(baseUrl);
    $("a[href='/redirect']").click();
    $("input[name='username']").setValue("Rustam");
    $("input[name='password']").setValue("1234");
    $("button[type='submit']").click();
    $(".header__title").shouldBe(Condition.visible)
        .shouldHave(text("Niffler. The coin keeper."));
    $$("table td").shouldHave(sizeGreaterThan(0));
    $$("table td").shouldHave(texts(
        "",
        "09 Feb 23",
        body.getAmount(),
        body.getCurrency(),
        body.getCategory(),
        body.getDescription()
    ));
  }

}
