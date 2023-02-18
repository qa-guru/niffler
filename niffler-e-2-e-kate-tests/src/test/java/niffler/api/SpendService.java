package niffler.api;

import config.App;
import niffler.model.SpendModel;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class SpendService {

    static final String ADD_SPEND_ENDPOINT = "/addSpend";
    static final String DELETE_SPEND_ENDPOINT = "/deleteSpends";

    public static SpendModel addSpend(SpendModel spend) {
        return given()
                    .contentType(JSON)
                    .baseUri(App.SPEND_URI)
                    .body(spend)
                .when()
                    .post(ADD_SPEND_ENDPOINT)
                    .then()
                    .statusCode(201)
                    .extract().as(SpendModel.class);
    }

    public static void deleteSpends(SpendModel spend) {
        given()
                .baseUri(App.SPEND_URI)
                .param("username", spend.getUsername())
                .param("ids", spend.getId())
            .when()
                .delete(DELETE_SPEND_ENDPOINT)
            .then()
                .statusCode(202);
    }
}
