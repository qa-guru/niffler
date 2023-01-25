package niffler.jupiter;

import io.restassured.http.ContentType;
import niffler.dto.DTOSpend;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static io.restassured.RestAssured.given;

public class MyExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        System.out.println("Тест пройден");
        DTOSpend spends = new DTOSpend();

        spends.setSpendDate("2023-01-25T06:01:27.144Z");
        spends.setCategory("Каток");
        spends.setAmount("44");
        spends.setDescription("Покатушки");
        spends.setCurrency("RUB");
        spends.setUsername("artem");


        given().header("Content-Type", ContentType.JSON)
                .and()
                .body(spends)
                .when().log().all()
                .post("http://127.0.0.1:8093/addSpend")
                .then()
                .statusCode(201);



    }
}
