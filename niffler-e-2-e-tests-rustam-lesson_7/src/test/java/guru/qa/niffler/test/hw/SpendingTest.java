package guru.qa.niffler.test.hw;

import static guru.qa.niffler.jupiter.extension.CreateUserExtension.Selector.NESTED;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.entity.userdata.CurrencyValues;
import guru.qa.niffler.page.BasePageRustam;
import guru.qa.niffler.page.MainPageRustam;
import guru.qa.niffler.test.other.BaseWebTest;
import io.qameta.allure.AllureId;
import guru.qa.niffler.jupiter.annotation.*;
import guru.qa.niffler.model.Spend7Json;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.ResourceLocks;


@Execution(ExecutionMode.SAME_THREAD)
public class SpendingTest extends BaseWebTest {

  private final MainPage mainPage = new MainPage();

  @Test
  @AllureId("50")
  @ResourceLocks({
      @ResourceLock("spend")
  })
  @ApiLoginNew(username = "dima", password = "12345")
  void checkLastWeekSpendingTest() throws Exception {
    Spend7Json expected = new Spend7Json();
    expected.setSpendDate(DateUtils.fromString("15 Feb 23"));
    expected.setAmount(800D);
    expected.setCurrency(CurrencyValues.USD);
    expected.setCategory("Бар");
    expected.setDescription("Коктейль");

    Selenide.open(MainPage.URL, MainPage.class)
        .getSpendingTable()
        .clickByButton("Last week")
        .checkTableContains(expected);
  }

  @Test
  @AllureId("51")
  @ResourceLock("spend")
  @ApiLoginNew(username = "dima", password = "12345")
  void deleteSpendingTest() {
    Selenide.open(MainPage.URL, MainPage.class)
        .getSpendingTable()
        .deleteSpending()
        .checkTable();
  }

  @Test
  @AllureId("317")
  @ApiLoginNew(nifflerUser = @GenerateUserNew(
      categories = {
          @GenerateCategoryNew("Магазин"),
          @GenerateCategoryNew("Кино")
      }, spends = {
      @GenerateSpendNew(category = "Кино", amount = 400.0, description = "Поход в кино"),
      @GenerateSpendNew(category = "Магазин", amount = 1_500.0, date = "25 FEB 23")
  })
  )
  void checkSpendingViaGenerateUserAnnotationTest(@UserNew(selector = NESTED) UserJson user) {
    Spend7Json[] expectedSpends = user.getSpends().toArray(new Spend7Json[0]);
    mainPage.open()
        .getSpendingTable()
        .checkTableContains(expectedSpends);
  }
}
