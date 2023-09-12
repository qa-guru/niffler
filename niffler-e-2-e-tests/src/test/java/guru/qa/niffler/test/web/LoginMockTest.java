package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.WelcomePage;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

@Disabled
public class LoginMockTest extends BaseWebTest {

    private WireMockServer wireMockServer = new WireMockServer(8089);

    @BeforeEach
    void configure() {
        wireMockServer.start();

        wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/currentUser"))
                .withQueryParam("username", matching(".*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-type", "application/json")
                        .withBody(
                                """
                                            {
                                                "id": "229fc371-2821-4795-81a5-0b26d3cd417e",
                                                "username": "{{request.query.username}}",
                                                "firstname": null,
                                                "surname": null,
                                                "currency": "RUB",
                                                "photo": null
                                            }
                                        """)
                ));
    }

    @AfterEach
    void stop() {
        wireMockServer.stop();
    }

    @Test
    @AllureId("800001")
    @DisplayName("WEB: Главная страница должна отображаться после логина новым юзером")
    @Tag("WEB")
    void mainPageShouldBeDisplayedAfterSuccessLogin() throws Exception {
        Selenide.open(WelcomePage.URL, WelcomePage.class)
                .doLogin()
                .fillLoginPage("barsik", "12345")
                .submit(new MainPage())
                .waitForPageLoaded();
    }
}
