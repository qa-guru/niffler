package guru.qa.niffler.test.hw_part_2;

import static io.restassured.RestAssured.given;

import guru.qa.niffler.model.UserJson;
import io.restassured.http.ContentType;

public class UserService {

  public static UserJson updateUserdata(UserJson user) {

    return given().log().all()
        .contentType(ContentType.JSON)
        .baseUri("http://127.0.0.1:8089")
        .body(user)
        .when()
        .post("/updateUserInfo")
        .then().log().all()
        .statusCode(200)
        .extract().as(UserJson.class);
  }

}
