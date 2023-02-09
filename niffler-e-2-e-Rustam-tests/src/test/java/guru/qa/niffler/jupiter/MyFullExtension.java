package guru.qa.niffler.jupiter;

import static guru.qa.niffler.hw1.enums.EndpointsData.SPEND_URL;
import static guru.qa.niffler.hw1.specs.Specs.request;
import static guru.qa.niffler.hw1.specs.Specs.response201;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import guru.qa.niffler.hw1.models.SpendTableDto;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class MyFullExtension implements
    AfterAllCallback,
    AfterEachCallback,
    AfterTestExecutionCallback,
    BeforeAllCallback,
    BeforeEachCallback,
    BeforeTestExecutionCallback {

  public static SpendTableDto body = new SpendTableDto();

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {
    System.out.println("  ### BeforeAllCallback");
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    System.out.println("        ### BeforeEachCallback");

    body.setSpendDate("2023-02-08T21:51:55.804+00:00");
    body.setCategory("Бар");
    body.setCurrency("RUB");
    body.setAmount("1000");
    body.setDescription("За QA Guru!!!");
    body.setUsername("Rustam");

    given()
        .spec(request)
        .body(body)
        .when().log().all()
        .post(SPEND_URL.title)
        .then()
        .spec(response201)
        .body(matchesJsonSchemaInClasspath("jsons/spend.json"))
        .extract().as(SpendTableDto.class);

  }

  @Override
  public void beforeTestExecution(ExtensionContext context) throws Exception {
    System.out.println("              ### BeforeTestExecutionCallback");
  }

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {
    System.out.println("              ### AfterTestExecutionCallback");
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    System.out.println("        ### AfterEachCallback");

  }

  @Override
  public void afterAll(ExtensionContext context) throws Exception {
    System.out.println("  ### AfterAllCallback");
  }

}
