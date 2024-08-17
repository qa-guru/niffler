package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

@Disabled
public class ProfileMockTest extends BaseWebTest {

  private final WireMockServer wireMockServer = new WireMockServer(
      new WireMockConfiguration()
          .port(8089)
          .globalTemplating(true)
          .stubCorsEnabled(true)
  );

  @BeforeEach
  void configure() {
    wireMockServer.start();
    wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/internal/users/current"))
        .withQueryParam("username", matching(".*"))
        .willReturn(okJson(
            """
                    {
                        "id": "{{randomValue type='UUID'}}",
                        "username": "{{request.query.username}}",
                        "fullname": "Tuchs Dmitrii",
                        "currency": "RUB"
                    }
                """
        )));
  }

  @AfterEach
  void stop() {
    wireMockServer.stop();
  }

  @Test
  @AllureId("800001")
  @DisplayName("WEB: Главная страница должна отображаться после логина новым юзером")
  @Tag("WEB")
  @GenerateUser
  void mainPageShouldBeDisplayedAfterSuccessLogin(@User(selector = User.Selector.METHOD) UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .waitForPageLoaded();

    Selenide.open(ProfilePage.URL, ProfilePage.class)
        .checkUsername(user.username())
        .checkName("Tuchs Dmitrii");
  }
}
