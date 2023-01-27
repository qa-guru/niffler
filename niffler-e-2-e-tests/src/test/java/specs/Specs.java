package specs;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.with;

public class Specs {

    public static RequestSpecification requestSpec =
            with()
                    .baseUri("http://127.0.0.1:8090")
                    .accept("application/json")
                    .contentType(ContentType.JSON);
}
