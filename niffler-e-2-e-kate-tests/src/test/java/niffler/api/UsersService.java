package niffler.api;

import niffler.config.App;
import niffler.model.UserJson;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class UsersService {

    static final String UPDATE_USER_ENDPOINT = "/updateUserInfo";

    public static UserJson updateUser(UserJson user) {
        return given().log().all()
                    .contentType(JSON)
                    .baseUri(App.USER_URI)
                    .body(user)
                .when()
                    .post(UPDATE_USER_ENDPOINT)
                    .then().log().all()
                    .statusCode(200)
                    .extract().as(UserJson.class);
    }
}
