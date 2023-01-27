package tests;

import models.requests.PostAddSpendRequest;
import models.responses.PostAddSpendResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pages.AuthPage;
import pages.MainPage;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static specs.Specs.requestSpec;

public class SpendingTableTests extends BaseTest {
    MainPage mainPage = new MainPage();
    AuthPage authPage = new AuthPage();

    @BeforeAll
    public static void addSomeSpend() {
        var addSpendResponse = given()
                .spec(requestSpec)
                .basePath("/addSpend")
                .auth().oauth2("-wKQQPevU_KRFbFZQxpksVi4M4yNlEMvNTyzY-OFFGPznGJul-WDvIslsFY9pcUuNz8dUC6a8WJy" +
                        "8tx0UR-EmDx5x4aT2esGfqpVCZESITA5_YxCyP2x-WtO2xsg9edsjRRcXLoPcBoXpTYtd7D_2Mxn8JrTuAZwiqXjqTk2H4ey" +
                        "FyesJulcH_L9mxS3p-R7A9FyFrLU520yFaN97WAEgW4iOrAbCRnhEFr0-CxCyiJ_1qVIeNN2hTUngHzLA")
                .body(PostAddSpendRequest.builder()
                        .amount("300")
                        .category("Рестораны")
                        .description("autotest for spending table")
                        .spendDate("2023-01-27T22:21:32.746+00:00").build())
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract().as(PostAddSpendResponse.class);

        assertThat(addSpendResponse.getAmount(), equalTo(300));
    }

    @Test
    public void spendingTableShouldHaveSpending() {
        mainPage.open();
        mainPage.selectLogin();
        authPage.authorizeWithCredentials(USER_LOGIN, USER_PASS);
        mainPage.spendingTableShouldHave("autotest for spending table");
    }
}
