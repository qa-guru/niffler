package niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.qameta.allure.AllureId;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.User;
import niffler.model.rest.CurrencyValues;
import niffler.model.rest.UserJson;
import niffler.page.MainPage;
import niffler.page.WelcomePage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static niffler.jupiter.extension.CreateUserExtension.Selector.METHOD;

//@WireMockTest(httpPort = 8089)
public class LoginWithoutUserDataTest extends BaseWebTest {

    private static final ObjectMapper om = new ObjectMapper();
    private static final WireMock wiremock = new WireMock("niffler-userdata", 8089);

    @BeforeAll
    static void configureMock() throws Exception {
        UserJson user = new UserJson();
        user.setId(UUID.randomUUID());
        user.setUsername("dima");
        user.setFirstname("Dmitrii");
        user.setSurname("Tuchs");
        user.setCurrency(CurrencyValues.KZT);

        wiremock.register(WireMock.get("/currentUser?username=dima")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(om.writeValueAsString(user))
                )
        );
    }

    @Test
    @AllureId("0")
    @GenerateUser(username = "dima", password = "12345")
    void mainPageShouldBeDisplayedAfterSuccessLogin(@User(selector = METHOD) UserJson user)  {
        Selenide.open(WelcomePage.URL, WelcomePage.class)
                .doLogin()
                .fillLoginPage(user.getUsername(), user.getPassword())
                .submit(new MainPage())
                .waitForPageLoaded();
    }
}
