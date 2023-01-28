package spend;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import model.SpendDto;

import static io.restassured.RestAssured.given;

public class SpendClient {

    public ValidatableResponse post(SpendDto body) {
        return given()
                .spec(requestSpec())
                .body(body)
                .post("/addSpend")
                .then();
    }

    public ValidatableResponse get(SpendDto body) {
        return given()
                .spec(requestSpec())
                .queryParams("username", body.getUsername())
                .get("/spends")
                .then();
    }

    private RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .setBaseUri("http://localhost:8093")
                .build();
    }
}
