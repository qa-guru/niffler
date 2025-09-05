package guru.qa.niffler.test.web;

import guru.qa.niffler.condition.Color;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.page.MainPage;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;

import java.awt.image.BufferedImage;

@Epic(" [WEB][niffler-ng-client]: Траты, скриншотные тесты")
@DisplayName(" [WEB][niffler-ng-client]: Траты, скриншотные тесты")
public class StatisticTest extends BaseWebTest {


  @AllureId("500026")
  @DisplayName("WEB: График трат отображается корректно без учета архивных категорий, от больших к меньшим")
  @ApiLogin(
      user = @GenerateUser(
          spends = {@GenerateSpend(
              category = "Обучение",
              name = "Обучение Advanced 2.0",
              amount = 79990
          ), @GenerateSpend(
              category = "Веселье",
              name = "Поездка на Алтай",
              amount = 129500
          )}
      )
  )
  @ScreenShotTest(expected = "expected-stat.png")
  void checkStatComponentTest(BufferedImage expected) throws Exception {
    new MainPage().getStatComponent()
        .checkStatisticBubblesContains("Веселье 129500 ₽", "Обучение 79990 ₽")
        .checkStatisticImage(expected)
        .checkBubbles(Color.yellow, Color.green);
  }

  @AllureId("500027")
  @DisplayName("WEB: График трат отображается корректно с учетом архивных категорий, от больших к меньшим")
  @ApiLogin(
      user = @GenerateUser(
          categories = {
              @GenerateCategory(name = "Поездки"),
              @GenerateCategory(name = "Бары"),
              @GenerateCategory(name = "Ремонт", archived = true),
              @GenerateCategory(name = "Страховка", archived = true)
          },
          spends = {
              @GenerateSpend(
                  category = "Поездки",
                  name = "В Москву",
                  amount = 9500
              ),
              @GenerateSpend(
                  category = "Бары",
                  name = "Бар Каламбур",
                  amount = 8000
              ),
              @GenerateSpend(
                  category = "Ремонт",
                  name = "Цемент",
                  amount = 10000
              ),
              @GenerateSpend(
                  category = "Страховка",
                  name = "ОСАГО",
                  amount = 3000
              )
          }
      )
  )
  @ScreenShotTest(expected = "expected-stat-archived.png", rewriteExpected = true)
  void statComponentShouldDisplayArchivedCategories(BufferedImage expected) throws Exception {
    new MainPage().getStatComponent()
        .checkStatisticBubblesContains("Поездки 9500 ₽", "Бары 8000 ₽", "Archived 13000 ₽")
        .checkStatisticImage(expected)
        .checkBubbles(Color.yellow, Color.green, Color.blue100);
  }
}
