package niffler.jupiter.lesson1;

import io.restassured.http.ContentType;

import niffler.dto.DTOSpend;
import org.junit.jupiter.api.extension.*;

import static io.restassured.RestAssured.given;

public class MyFullExtension implements AfterAllCallback,
        AfterEachCallback,
        BeforeAllCallback,
        BeforeEachCallback,
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback {

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        System.out.println("### AfterAllCallback");
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        System.out.println("      ### BeforeEachCallback");
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
            System.out.println("        ###beforeAll");

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
                .when()
                .post("http://127.0.0.1:8093/addSpend")
                .then()
                .statusCode(201);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        System.out.println("      ### AfterEachCallback");
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        System.out.println("              ### BeforeTestExecutionCallback");
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        System.out.println("               ### AfterTestExecutionCallback");
    }
}
