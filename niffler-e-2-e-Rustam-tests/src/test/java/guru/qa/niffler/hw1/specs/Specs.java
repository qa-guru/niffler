package guru.qa.niffler.hw1.specs;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;
import static io.restassured.http.ContentType.JSON;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specs {

  public static RequestSpecification request;

  static {
    request = with()
        .log().all()
        .contentType(JSON);
  }

  public static ResponseSpecification response201 = new ResponseSpecBuilder()
      .log(STATUS)
      .log(BODY)
      .expectStatusCode(201)
      .build();
}
