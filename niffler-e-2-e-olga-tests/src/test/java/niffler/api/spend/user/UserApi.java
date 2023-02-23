package niffler.api.spend.user;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;
import niffler.api.base.BaseApi;
import niffler.api.spend.dto.UserDto;
import niffler.config.app.AppProperties;

import static io.restassured.RestAssured.given;


public final class UserApi extends BaseApi {

    private final static RequestSpecification REQUEST_SPEC = DEFAULT_REQUEST_SPEC
            .baseUri(AppProperties.USERDATA_APP_URI);

    @Step("User api: update user information")
    public static UserDto update(UserDto user) {
        Allure.addAttachment("Update user", user.toString());
        return given().spec(REQUEST_SPEC)
                .body(user.toJson())
                .when()
                .post(UserEndpoint.UPDATE)
                .then().spec(DEFAULT_RESPONSE_SPEC)
                .statusCode(200)
                .extract().as(UserDto.class);
    }

    @Step("User api: get user information")
    public static UserDto currentUser(String userName) {
        return given().spec(REQUEST_SPEC)
                .param("username", userName)
                .when()
                .get(UserEndpoint.GET_INFO)
                .then().spec(DEFAULT_RESPONSE_SPEC)
                .statusCode(200)
                .extract().as(UserDto.class);
    }

}
