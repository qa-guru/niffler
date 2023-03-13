package niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.qameta.allure.AllureId;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.User;
import niffler.model.CurrencyValues;
import niffler.model.UserJson;
import niffler.page.WelcomePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static niffler.jupiter.extension.CreateUserExtension.Selector.METHOD;

//@WireMockTest(httpPort = 8089)
public class LoginWithoutUserDataTest extends BaseTest {

    private static ObjectMapper om = new ObjectMapper();
    private UserJson user = new UserJson();


    private WireMock wiremock = new WireMock("niffler-userdata", 8089);

    @BeforeEach
    void configureUser() {
        user.setId(UUID.randomUUID());
        user.setUserName("dima");
        user.setFirstname("Dmitrii");
        user.setSurname("Tuchs");
        user.setCurrency(CurrencyValues.KZT);
    }

    // http://127.0.0.1:8090/currentUser GATEWAY
    // http://127.0.0.1:8090/currentUser?username=dima USERDATA
    // {"id":"eef2fec4-78bb-4740-a675-8e908ece5d83","username":"dima","firstname":"Dmitrii","surname":"Tuchs","currency":"KZT","photo":""}

    // http://127.0.0.1:8090/invitations GATEWAY
    // http://127.0.0.1:8090/invitations?username=dima USERDATA
    // []

    @Disabled
    @Test
    @AllureId("2000")
    void mainPageShouldBeDisplayedAfterSuccessLogin() throws Exception {
        wiremock.register(WireMock.get("/currentUser?username=dima")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(om.writeValueAsString(user))
                )
        );

        Selenide.open(WelcomePage.URL, WelcomePage.class)
                .doLogin()
                .fillLoginPage("dima", "12345")
                .submit()
                .waitForPageLoaded();
    }
}
